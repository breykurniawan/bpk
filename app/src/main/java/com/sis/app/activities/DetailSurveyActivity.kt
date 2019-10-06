package com.sis.app.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sis.app.R
import com.sis.app.adapters.QuestionListAdapter
import com.sis.app.models.identity.DataResponse
import com.sis.app.models.identity.RespondentData
import com.sis.app.models.surveyQuestion.QuestionReference
import com.sis.app.models.surveyData.SubSurvey
import com.sis.app.models.surveyData.Survey
import com.sis.app.models.surveyQuestion.Answer
import com.sis.app.models.surveyQuestion.AnswerModel
import com.sis.app.networks.Api
import com.sis.app.others.TinyDB
import com.sis.app.others.Utility
import kotlinx.android.synthetic.main.activity_detail_survey.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class DetailSurveyActivity : AppCompatActivity() {

    private var viewAdapter: QuestionListAdapter? = null
    private var answer: MutableList<AnswerModel> = mutableListOf()
    private var answerSend: MutableList<AnswerModel> = mutableListOf()
    private var body: Survey? = null
    private var kuisioner: List<SubSurvey>? = listOf()
    private var data: RespondentData? = null
    private var imageTaken: MultipartBody.Part? = null

    private var idx: Int = -1
    private var idxNow = -1
    private var id_responden: Int = -1
    private var id_user: Int = -1
    private var idPertanyaanGambar = -1
    private var storage: Int = 0
    private var camera: Int = 0
    //    private var id_kuisioner: Int = -1

    private var currentPhotoPath = ""

    private var finishing: Boolean = false
    private var respondentDataSent: Boolean = false
    private var answerSent: Boolean = false
    private var photoSent: Boolean = false
    private var answerInserted: Boolean = false
    private var allowTakePhoto: Boolean = false
    private var masyarakat: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_survey)
        setSupportActionBar(toolbar)
        storage = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        camera = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)

        data = intent.getParcelableExtra("respondenData")
        id_user = TinyDB(applicationContext).getInt("idSurveyor")
        masyarakat = intent.getBooleanExtra("masyarakat", false)
        val id_kuisioner = intent.getIntExtra("id_kuisioner", -1)

        getKuisioner(id_kuisioner)

        next.setOnClickListener {
            if (finishing) attemptFinish()
            else nextPart()
        }
        take_photo.setOnClickListener {
            checkPermission()
            if (storage == PackageManager.PERMISSION_GRANTED && camera == PackageManager.PERMISSION_GRANTED) {
                takePicture()
//                dispatchTakePictureIntent()

            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, Utility.TAKE_PHOTOS)
            }
        }
    }

    override fun onBackPressed() {
        warnDialog()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            val ph: Bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            val res = Utility().checkRatio(ph.width.toFloat(), ph.height.toFloat()).split(" ")
            val result = Utility().decodeSampledBitmapFromPath(
                currentPhotoPath,
                res[0].toInt(),
                res[1].toInt()
            )
            photo.setImageBitmap(result)
            container_foto.visibility = View.VISIBLE
            val photoFile: File? = try {
                File(currentPhotoPath)
            } catch (ex: IOException) {
                logging("error ${ex.message}")
                null
            }
            if (photoFile != null) {
                if (photoFile.exists()) {
                    photoFile.delete()
                }
            }
            val fo = FileOutputStream(photoFile)
            result.compress(Bitmap.CompressFormat.JPEG, 100, fo)
            fo.flush()
            fo.close()
        } catch (ex: IOException) {
            logging("gagal menyimpan foto ${ex.message}")
            showSnack("Gagal Menyimpan Foto")
        }
    }

    private fun nextPart() {
        if (idx != 0) {
            rv_list_bagian.visibility = View.GONE
            progress.visibility = View.VISIBLE
            if (checkAnswer()) {
                idxNow++
                if (idxNow == idx) {
                    attemptFinish()
                } else if (idxNow == (idx - 1)) {
                    next.text = "selesai"
                    populateData()
                } else {
                    populateData()
                }
            } else {
                rv_list_bagian.visibility = View.VISIBLE
                progress.visibility = View.GONE
            }
        } else {
            finish()
        }
    }

    private fun attemptFinish() {
        if (!finishing) {
            finishing = true
            insertAnswer()
            println("jumlah jawaban ${answer.size}")
        }
        title_.text = "Mengirim..."
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) scroll_layout.foreground =
            ColorDrawable(resources.getColor(R.color.grey_transparent))
        else overlay.visibility = View.VISIBLE
        progress.visibility = View.VISIBLE
        container_foto.visibility = View.GONE
        take_photo.visibility = View.GONE
        next.isEnabled = false
        if (!respondentDataSent) {
            sendRespondentData()
        } else {
            if (!answerSent) sendQuestionnaire()
            else if (masyarakat) if (!photoSent) sendPhoto()
        }
    }

    private fun sendQuestionnaire() {
        val call = Api().getInstance().sendAnswer(Answer(answerSend))
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showSnack("Gagal Mengirim Data")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) scroll_layout.foreground = null
                else overlay.visibility = View.GONE
                progress.visibility = View.GONE
                title_.text = "Proses Menyimpan Gagal"
                next.isEnabled = true

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    showSnack("Mengirim...")
                    answerSent = true
                    if (masyarakat) {
                        sendPhoto()
                    }
                } else {
                    showSnack("Gagal Mengirim Data : ${response.message()}")
                    logging("Gagal Mengirim Data : $response")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) scroll_layout.foreground =
                        null
                    else overlay.visibility = View.GONE
                    progress.visibility = View.GONE
                    next.isEnabled = true
                    title_.text = "Proses Menyimpan Gagal"
                }
            }
        })
    }

    private fun sendPhoto() {
        println("sending photo")
        val requestBody =
            RequestBody.create(MultipartBody.FORM, File(currentPhotoPath))
        imageTaken = MultipartBody.Part.createFormData(
            "foto",
            File(currentPhotoPath)?.name,
            requestBody
        )

        val dataGambar = mapOf(
            "id_pertanyaan" to idPertanyaanGambar,
            "id_responden" to id_responden,
            "id_user" to id_user
        )
        val sendPhoto = Api().getInstance().sendImageRespondent(imageTaken!!, dataGambar)
        sendPhoto.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showSnack("Proses Upload Gagal")
                logging("Proses Upload Gagal ${t.message}")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) scroll_layout.foreground =
                    null
                else overlay.visibility = View.GONE
                progress.visibility = View.GONE
                title_.text = "Proses Menyimpan Gagal"
                next.isEnabled = true
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    logging("Berhasil Upload Foto")
                    showSnack("Data Tersimpan")
                    photoSent = true
                    Handler().postDelayed({
                        deletePhotoAfterSending()
                        finish()
                    }, 2000)
                } else {
                    showSnack("Upload Foto Gagal: ${response.message()}")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) scroll_layout.foreground =
                        null
                    else overlay.visibility = View.GONE
                    progress.visibility = View.GONE
                    title_.text = "Proses Menyimpan Gagal"
                    next.isEnabled = true
                }
            }

        })

    }

    private fun sendRespondentData() {

        val sendData = Api().getInstance().sendDataRespondent(
            data!!.nama,
            data!!.alamat,
            data!!.jenis_kelamin,
            data!!.no_hp,
            data!!.nama_instansi,
            data!!.tipe_stakeholder!!,
            data!!.nama_stakeholder!!,
            data!!.domisili!!,
            data!!.usia,
            data!!.pendidikan
        )

        sendData.enqueue(object : Callback<DataResponse> {
            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) scroll_layout.foreground =
                    null
                else overlay.visibility = View.GONE
                progress.visibility = View.GONE
                showSnack("Gagal Mengirim Data Responden")
                logging("Cannot send data: ${t.message}")
                title_.text = "Proses Menyimpan Gagal"
            }

            override fun onResponse(
                call: Call<DataResponse>,
                response: Response<DataResponse>
            ) {
                if (response.isSuccessful) {
                    id_responden = response.body()!!.id_responden
                    respondentDataSent = true
                    if (!answerInserted) {
                        answerInserted = true
                        insertLastAnswer()
                    }
                    sendQuestionnaire()
                } else {
                    showSnack("Gagal Menerima ID Responden")
                    logging("problem ${response}")
                    logging("IDresponden = ${response.body()?.id_responden}")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) scroll_layout.foreground =
                        null
                    else overlay.visibility = View.GONE
                    progress.visibility = View.GONE
                    title_.text = "Proses Menyimpan Gagal"
                }
            }
        })
    }

    private fun checkAnswer(): Boolean {
        viewAdapter
            ?.list!!.forEach {
            if (it.id_pertanyaan != -1) {
                if (it.tipe.equals(Utility.QUESTION_TEXTAREA)) {
                    if (it.nilai.equals("")) {
                        Log.w(
                            DetailSurveyActivity::class.java.simpleName,
                            "jawaban tidak diisi: idPertanyaan ${it.id_pertanyaan}"
                        )
                        showSnack("Pertanyaan ${it.nomor} Tidak Diisi")
                        return false
                    }
                } else if (it.tipe.equals(Utility.QUESTION_SCALE)) {
                    if (it.nilai?.toInt() == -1) {
                        Log.w(
                            DetailSurveyActivity::class.java.simpleName,
                            "jawaban tidak diisi: idPertanyaan ${it.id_pertanyaan}"
                        )
                        showSnack("Pertanyaan ${it.nomor} Tidak Diisi")
                        return false
                    }
                }
            }
        }

        if (allowTakePhoto) {
            if (TextUtils.isEmpty(currentPhotoPath)) {
                showSnack("Anda belum mengambil foto dengan responden")
                return false
            }
        }
        return true
    }

    private fun getKuisioner(id_kuisioner: Int = -1) {
        if (id_kuisioner == -1) {
            Log.w(SubSurveySection::class.java.simpleName, "No id defined here")
            return
        }

        val call: Call<Survey> = Api().getInstance().getQuestionnaire(id_kuisioner)
        call.enqueue(object : retrofit2.Callback<Survey> {
            override fun onFailure(call: Call<Survey>, t: Throwable) {
                Handler().postDelayed({
                    progress.visibility = View.GONE
                    load_off.visibility = View.VISIBLE
                }, 3000)
            }

            override fun onResponse(call: Call<Survey>, response: Response<Survey>) {
                if (response.isSuccessful) {
                    progress.visibility = View.GONE
                    next.visibility = View.VISIBLE
                    body = response.body()
                    populateData()
                }
            }

        })
    }

    private fun populateData() {
        if (body?.bagian?.size == 0) {
            title_.text = "Tidak Ada Kuisioner Yang Harus Diisi"
            next.text = "selesai"
        } else {
            if (idx == -1) {
                idx = body?.bagian?.size!!
                idxNow = 0
            } else {
                insertAnswer()
            }
            title_.text = body?.bagian?.get(idxNow)?.judul
            var listPertanyaan = mutableListOf<QuestionReference>()
            val sub = body?.bagian?.get(idxNow)?.subs

            var i = 1;
            var j = 1;
            sub?.forEach { s ->
                listPertanyaan.add(
                    QuestionReference(-1, -1, s.judul, Utility.QUESTION_SECTION, "", "${i}.")
                )
                i++
                s.pertanyaan.forEach { q ->
                    if (q.tipe.equals("rating")) {
                        listPertanyaan.add(
                            QuestionReference(
                                q.id_pertanyaan,
                                q.id_bagian,
                                q.pertanyaan,
                                Utility.QUESTION_SCALE,
                                "-1",
                                "${j}."
                            )
                        )
                        j++
                    } else if (q.tipe.equals("text")) {
                        listPertanyaan.add(
                            QuestionReference(
                                q.id_pertanyaan,
                                q.id_bagian,
                                q.pertanyaan,
                                Utility.QUESTION_TEXTAREA,
                                "",
                                "${j}."
                            )
                        )
                        j++
                    } else if (q.tipe.equals("photo")) {
                        allowTakePhoto = true
                        take_photo.visibility = View.VISIBLE
                        take_photo.text = q.pertanyaan
                        idPertanyaanGambar = q.id_pertanyaan
                    }
                }
            }
            viewAdapter = QuestionListAdapter(listPertanyaan)
            rv_list_bagian.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = viewAdapter
            }
            Handler().postDelayed({
                rv_list_bagian.visibility = View.VISIBLE
                progress.visibility = View.GONE
            }, 1000)
        }
    }

    private fun warnDialog() {
        val dialog: AlertDialog.Builder? = AlertDialog.Builder(this@DetailSurveyActivity)
        dialog?.setMessage("Batal Mengisi Kuisioner?")
            ?.setPositiveButton("Ya", { dialog, id ->
                finish()
            })
            ?.setNegativeButton("Tidak", { dialog, id ->
                dialog.dismiss()
            })
        dialog?.create()?.show()
    }

    private fun logging(msg: String) = Log.w(InputIdentityActivity::class.java.simpleName, msg)

    private fun insertAnswer() {
        viewAdapter?.list!!.forEach {
            if (it.id_pertanyaan != -1) {
                answer.add(AnswerModel(it.id_pertanyaan, id_responden, id_user, it.nilai))
            }
        }
    }

    private fun insertLastAnswer() {
        answer.forEach {
            answerSend.add(AnswerModel(it.id_pertanyaan, id_responden, it.id_user, it.nilai))
        }
    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    logging("error ${ex.message}")
                    null
                }
                if (photoFile != null) {
                    photoFile.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.sis.app.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, Utility.TAKE_PHOTOS)
                    }
                } else {
                    showSnack("Gagal Menyimpan Foto")
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("dd-MM-yyyy HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "RESPONDEN_MASYARAKAT_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun checkPermission() {

        if (storage != PackageManager.PERMISSION_GRANTED
        ) ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            Utility.WRITE_EXTERNAL
        )
        if (camera != PackageManager.PERMISSION_GRANTED
        ) ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            Utility.ACCESS_CAMERA
        )
        if (storage == PackageManager.PERMISSION_DENIED) showSnack("Akses storage ditolak")
        if (camera == PackageManager.PERMISSION_DENIED) showSnack("akses kamera ditolak")
    }

    private fun showSnack(msg: String) =
        Snackbar.make(parent_layout, msg, Snackbar.LENGTH_LONG).show()

    private fun deletePhotoAfterSending() {
        val photoFile: File? = try {
            File(currentPhotoPath)
        } catch (ex: IOException) {
            logging("error ${ex.message}")
            null
        }
        if (photoFile != null) {
            if (photoFile.exists()) {
                photoFile.delete()
            }
        }
    }
}
