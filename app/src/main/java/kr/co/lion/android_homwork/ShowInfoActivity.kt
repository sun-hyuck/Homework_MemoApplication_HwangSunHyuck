package kr.co.lion.android_homwork

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call.Details
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kr.co.lion.android_homwork.databinding.ActivityShowInfoBinding
import kr.co.lion.android_homwork.MemoData

class ShowInfoActivity : AppCompatActivity() {

    lateinit var activityShowInfoBinding: ActivityShowInfoBinding
    lateinit var editActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityShowInfoBinding = ActivityShowInfoBinding.inflate(layoutInflater)
        setContentView(activityShowInfoBinding.root)

        initData()
        setToolbar()
        setView()

    }
    fun initData() {
        // InputActivitiy 런처
        val contract1 = ActivityResultContracts.StartActivityForResult()
        editActivityLauncher = registerForActivityResult(contract1) {
            // 작업 결과가 ok 라면
            if (it.resultCode == RESULT_OK) {
                // 전달된 Intent객체가 있다면
                if (it.data != null) {
                    activityShowInfoBinding.apply {
                    // 메모 객체를 추출한다.
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
                        val title = it.data!!.getStringExtra("title")
                        val detail = it.data!!.getStringExtra("detail")
                        textInputEditView1.setText("$title")
                        textInputEditView3.setText("$detail")

                    } else {
                        val title = it.data!!.getStringExtra("title")
                        val detail = it.data!!.getStringExtra("detail")
                        textInputEditView1.setText("$title")
                        textInputEditView3.setText("$detail")
                    }
                    }
                }
            }
        }
    }

    // 툴바 설정
    fun setToolbar() {
        activityShowInfoBinding.apply {
            toolbarShowInfo.apply {
                // 타이틀
                title = "메모 보기"
                // Back
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.menu_show_info)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_delete -> {
                            deleteData()
                        }

                        R.id.menu_edit -> {
                            val memoData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                intent.getParcelableExtra("memoData", MemoData::class.java)
                            } else {
                                intent.getParcelableExtra("memoData")
                            }
                            val editIntent = Intent(this@ShowInfoActivity, EditActivity::class.java)
                            editIntent.putExtra("memoData", memoData)
                            editActivityLauncher.launch(editIntent)
                        }
                    }
                    true
                }

            }
        }
    }


    // 뷰 설정
    fun setView() {
        activityShowInfoBinding.apply {
            val memoData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("memoData", MemoData::class.java)
            } else {
                intent.getParcelableExtra("memoData")
            }
            // TextViewinputLayout
            textInputEditView1.apply {

                setText("제목 : ${memoData?.title}")

            }
            textInputEditView2.apply {
                setText("날짜 : ${memoData?.timestamp}")
            }
            textInputEditView3.apply {
                setText("내용 : ${memoData?.detail}")
            }
        }
    }


    fun deleteData() {
        activityShowInfoBinding.apply {
            val memoData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("memoData", MemoData::class.java)
            } else {
                intent.getParcelableExtra("memoData")
            }
            Log.d("memodata2", "야옹${memoData}")

            val resultIntent = Intent()
            resultIntent.putExtra("memoData", memoData)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}


