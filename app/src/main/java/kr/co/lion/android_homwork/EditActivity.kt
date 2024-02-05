package kr.co.lion.android_homwork

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kr.co.lion.android_homwork.databinding.ActivityEditBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class EditActivity : AppCompatActivity() {
    lateinit var activityEditBinding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityEditBinding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(activityEditBinding.root)

        setToolbar()
        setView()
    }

    fun setToolbar() {
        activityEditBinding.apply {
            toolbarEdit.apply {
                // Back
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                inflateMenu(R.menu.menu_edit)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_edit_done -> {
                            saveMemo()
                        }
                    }
                    true
                }

            }
        }
    }
    fun setView() {
        activityEditBinding.apply {
            val memoData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Log.d("datamemo", "datamemo")
                intent.getParcelableExtra("memoData", MemoData::class.java)
            } else {
                Log.d("datamemo2", "datamemo")
                intent.getParcelableExtra("memoData")

            }
            // TextViewinputLayout
            textInputEditTitle.apply {
                setText("제목 : ${memoData?.title}")

            }
            textInputEditDetail.apply {
                setText("내용 : ${memoData?.detail}")

            }
        }
    }
    fun saveMemo(){
        activityEditBinding.apply {
            val title = textInputEditTitle.text.toString()
            val detail = textInputEditDetail.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra("title", title )
            resultIntent.putExtra("detail", detail )
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}