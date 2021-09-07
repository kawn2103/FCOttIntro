package kst.app.fcottintro

import android.app.Activity
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.AppBarLayout
import kst.app.fcottintro.databinding.ActivityMainBinding
import java.lang.Math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var isGateringMotionAnimation:Boolean= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makeStatusBarTransparent()
        initAppBar()
        initInsetMargin()

        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {
            Log.d("gwan2103_1","scrollY >>>>> ${binding.scrollView.scrollY}")
            Log.d("gwan2103_1","dpToPx >>>>> ${150f.dpToPx(this).toInt()}")
            if (binding.scrollView.scrollY > 150){
                Log.d("gwan2103_2","scrollY >>>>> ${binding.scrollView.scrollY}")
                Log.d("gwan2103_2","dpToPx >>>>> ${150f.dpToPx(this).toInt()}")
                if (!isGateringMotionAnimation){
                    Log.d("gwan2103_3","scrollY >>>>> ${binding.scrollView.scrollY}")
                    Log.d("gwan2103_3","dpToPx >>>>> ${150f.dpToPx(this).toInt()}")
                    binding.gatheringDigitalThingsLayout.transitionToEnd()
                    binding.buttonShownLayout.transitionToEnd()
                }
            } else {
                Log.d("gwan2103_4","scrollY >>>>> ${binding.scrollView.scrollY}")
                Log.d("gwan2103_4","dpToPx >>>>> ${150f.dpToPx(this).toInt()}")
                if (!isGateringMotionAnimation){
                    Log.d("gwan2103_5","scrollY >>>>> ${binding.scrollView.scrollY}")
                    Log.d("gwan2103_5","dpToPx >>>>> ${150f.dpToPx(this).toInt()}")
                    binding.gatheringDigitalThingsLayout.transitionToStart()
                    binding.buttonShownLayout.transitionToStart()
                }
            }
        }

        binding.gatheringDigitalThingsLayout.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
                isGateringMotionAnimation = true
            }
            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float)  = Unit
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                isGateringMotionAnimation = false
            }
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) = Unit
        })
    }

    private fun initAppBar(){
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val topPadding = 300f.dpToPx(this)
            val realAlphaScrollHeight = appBarLayout.measuredHeight - appBarLayout.totalScrollRange
            val abstractOffset = abs(verticalOffset)

            val realAlphaVerticalOffset = if (abstractOffset - topPadding < 0) 0f else abstractOffset - topPadding

            if (abstractOffset < topPadding) {
                binding.toolbarBackgroundView.alpha = 0f
                return@OnOffsetChangedListener
            }
            val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
            binding.toolbarBackgroundView.alpha = 1 - (if (1 - percentage * 2 < 0) 0f else 1 - percentage * 2)
        })
        initActionBar()
    }


    private fun initActionBar() = with(binding) {
        toolbar.navigationIcon = null
        toolbar.setContentInsetsAbsolute(0, 0)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setHomeButtonEnabled(false)
            it.setDisplayHomeAsUpEnabled(false)
            it.setDisplayShowHomeEnabled(false)
        }
    }

    private fun initInsetMargin() = with(binding) {
        ViewCompat.setOnApplyWindowInsetsListener(cordinator) { v: View, insets: WindowInsetsCompat ->
            val params = v.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = insets.systemWindowInsetBottom
            toolbarContainer.layoutParams = (toolbarContainer.layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(0, insets.systemWindowInsetTop, 0, 0)
            }
            collapsingToolBarContainer.layoutParams = (collapsingToolBarContainer.layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(0, 0, 0, 0)
            }

            insets.consumeSystemWindowInsets()
        }
    }
}

fun Float.dpToPx(context: Context): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)

fun Activity.makeStatusBarTransparent() {
    with(window) {
        decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = Color.TRANSPARENT
    }
}