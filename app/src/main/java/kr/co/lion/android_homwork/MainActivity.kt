package kr.co.lion.android_homwork

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.android_homwork.databinding.ActivityMainBinding
import kr.co.lion.android_homwork.databinding.RowMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.contracts.contract


class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding
    // inputActivity 런처
    lateinit var inputActivityLauncher:ActivityResultLauncher<Intent>
    // ShowInfoActivity의 런처
    lateinit var showInfoActivityLauncher:ActivityResultLauncher<Intent>

    val memoList = mutableListOf<MemoData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setToolbar()
        setView()
        initData()
    }
    // 기본 데이터 및 객체 세팅
    fun initData(){
        // InputActivitiy 런처
        val contract1 = ActivityResultContracts.StartActivityForResult()
            inputActivityLauncher = registerForActivityResult(contract1) {
                // 작업 결과가 ok 라면
                if(it.resultCode == RESULT_OK) {
                    // 전달된 Intent객체가 있다면
                    if(it.data != null){
                        // 메모 객체를 추출한다.
                        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU){
                            val memoData = it.data?.getParcelableExtra("memoData", MemoData::class.java)
                            memoList.add(memoData!!)
                        }else{
                            val memoData = it.data?.getParcelableExtra<MemoData>("memoData")
                            memoList.add(memoData!!)
                            activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                        }
                    }
                }

            }
        val contract2 = ActivityResultContracts.StartActivityForResult()
        showInfoActivityLauncher = registerForActivityResult(contract2){
            // 작업 결과가 ok 라면
            if(it.resultCode == RESULT_OK) {
                // 전달된 Intent객체가 있다면
                if(it.data != null){
                    // 메모 객체를 추출한다.
                    if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU){
                        val memoData = it.data?.getParcelableExtra("memoData", MemoData::class.java)
                       // Log.d("memodata3", "응애${memoData.toString()}")
                        memoList.removeAll { it.title == memoData!!.title && it.detail == memoData!!.detail && it.timestamp == memoData!!.timestamp }
                    }else{
                        val memoData = it.data?.getParcelableExtra<MemoData>("memoData")
                     // Log.d("memodata", "애응${memoData.toString()}")
                        memoList.removeAll { it.title == memoData!!.title && it.detail == memoData!!.detail && it.timestamp == memoData!!.timestamp }
                     //   Log.d("removedate","응애응애${memoList}")
                        activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                    }
                }
            }

        }
    }
    fun setToolbar(){
        activityMainBinding.apply {
            toolbarMain.apply {
                // 타이틀
                title = "메모 관리"
                // 메뉴
                inflateMenu(R.menu.menu_main)
                // 메뉴의 리스너
                setOnMenuItemClickListener {
                    // 메뉴의 id로 분기한다
                    when(it.itemId){
                        //추가 메뉴
                        R.id.menu_main_add -> {
                            // InputActivity를 실행한다.
                            val inputIntent = Intent(this@MainActivity, InputActivity::class.java)
                            inputActivityLauncher.launch(inputIntent)
                        }
                    }
                    true
                }
            }
        }
    }
    // View 구성
    fun setView(){
        activityMainBinding.apply {
            // RecyclerView
            recyclerViewMain.apply {
                // 어뎁터 설정
                adapter = RecyclerViewMainAdapter()
                // 레이아웃 매니저
                layoutManager = LinearLayoutManager(this@MainActivity)
                val deco = MaterialDividerItemDecoration(this@MainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }
    // RecyclerView의 어뎁터
    inner class RecyclerViewMainAdapter : RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolderMain>(){
        // ViewHolder
        inner class ViewHolderMain(rowMainBinding: RowMainBinding) : RecyclerView.ViewHolder(rowMainBinding.root){
                val rowMainBinding:RowMainBinding

                init{
                    this.rowMainBinding = rowMainBinding
                    // 항목 클릭시 전체가 클릭이 될 수 있도록
                    // 가로 세로 길이를 설정해준다.
                    this.rowMainBinding.root.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    // 항목을 눌렀을 때의 리스너
                    this.rowMainBinding.root.setOnClickListener{
                        val showInfoIntent = Intent(this@MainActivity, ShowInfoActivity::class.java)

                        // 선택한 항목 번째의 학생 객체를 Intent에 담아준다.
                        showInfoIntent.putExtra("memoData", memoList[adapterPosition])
                        showInfoActivityLauncher.launch(showInfoIntent)
                    }
                }

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater)
            val viewHolderMain = ViewHolderMain(rowMainBinding)

            return viewHolderMain
        }

        override fun getItemCount(): Int {
            return memoList.size
        }

        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
            holder.rowMainBinding.textViewRowMainTitle.text = "${memoList[position].title}"
            holder.rowMainBinding.textViewRowMainDate.text = "${memoList[position].timestamp}"
        }
    }
}

