package kr.co.lion.android_homwork

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.android_homwork.databinding.ActivityInputBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import kotlin.concurrent.thread

class InputActivity : AppCompatActivity() {

    lateinit var activityInputBinding: ActivityInputBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityInputBinding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(activityInputBinding.root)

        setToolbar()
        setView()
    }

    fun setToolbar(){
        activityInputBinding.apply {
            toolbarInput.apply{
                // 타이틀
                title = "메모 작성"

                // Back
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.menu_input)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_input_done -> {
                            processInputDone()
                        }
                    }
                    true
                }
            }
        }
    }
    // View 설정
    fun setView() {
        activityInputBinding.apply {
            // 뷰에 포커스를 준다.
            textFieldInputTitle.requestFocus()

            showSoftInput(textFieldInputTitle)
            thread {
                SystemClock.sleep(300)
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(textFieldInputTitle, 0)
            }
            // 엔터키를 누르면 입력 완료 처리를 한다
            textFieldInputDetail.setOnKeyListener { view, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    processInputDone()
                    true
                }
                false
            }
        }
    }
        fun processInputDone(){
          // Toast.makeText(this@InputActivity, "눌러졌습니다.", Toast.LENGTH_SHORT).show()
            activityInputBinding.apply {
                val title = textFieldInputTitle.text.toString()
                val detail = textFieldInputDetail.text.toString()
                val formatDate = SimpleDateFormat("yyyy-MM-DD HH:mm")
                val calendar = Calendar.getInstance().time
                val timestamp = formatDate.format(calendar)

                    // 입력 검사
                if(title.isEmpty()){
                    showDialog("제목 입력 오류", "제목을 입력해주세요", textFieldInputTitle)
                    return
                }
                if(detail.isEmpty()){
                    showDialog("내용 입력 오류", "내용을 입력해주세요", textFieldInputDetail)
                    return
                }

                // 입력받은 정보를 객체에 담아준다.
                val memoData = MemoData(title, detail, timestamp)

                Snackbar.make(activityInputBinding.root, "저장되었습니다.", Snackbar.LENGTH_SHORT).show()
                val resultIntent = Intent()
                resultIntent.putExtra("memoData", memoData)

                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }

    // 다이얼로그를 보여주는 메서드
    fun showDialog(title:String, message:String, focusView: TextInputEditText){
        // 다이얼로그를 보여준다.
        val builder = MaterialAlertDialogBuilder(this@InputActivity).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                focusView.setText("")
                focusView.requestFocus()
                showSoftInput(focusView)
            }
        }
        builder.show()
    }

    fun showSoftInput(focusView:TextInputEditText){
        thread {
            SystemClock.sleep(1000)
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(focusView, 0)
        }
    }
}

