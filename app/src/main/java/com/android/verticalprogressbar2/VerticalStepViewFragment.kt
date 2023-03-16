package com.android.verticalprogressbar2

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import com.android.stepview.VerticalStepView

class VerticalStepViewFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    var count = 0
    private var mSetpview0: VerticalStepView? = null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_vertical_step_view, container, false)



        mSetpview0 = view.findViewById<View>(R.id.step_view0) as VerticalStepView
        var sv = view.findViewById<View>(R.id.sv1) as ScrollView


        var btn0 = view.findViewById<Button>(R.id.btn0) as Button
        var btn1 = view.findViewById<Button>(R.id.back_btn) as Button
        val list0: MutableList<String> = ArrayList()
        list0.add("Activating\n1/5")
        list0.add("Syncing\n2/5")
        list0.add("Pairing\n3/5")
        list0.add("GPS\n4/5")
        list0.add("Activation completed\n5/5")
//        list0.add("")
        mSetpview0!!.setStepsViewIndicatorComplectingPosition(list0.size - 4)
//        mSetpview0!!.setStepsViewIndicatorUnCompletedLineColor(requireActivity().getColor(R.color.colorAccent))

            .reverseDraw(false) //default is true
            .setTextSize(16)
            .setStepViewTexts(list0) //总步骤


        btn0.setOnClickListener(View.OnClickListener {

            when (count) {
                0 -> {
                    mSetpview0!!.setStepsViewIndicatorComplectingPosition(list0.size - 3) //设置完成的步数

                    count++
                    Log.d("TAG", "count1++: "+count)
                }
//
                1 -> {
                    mSetpview0!!.setStepsViewIndicatorComplectingPosition(list0.size - 2) //设置完成的步数

                    count++
                    Log.d("TAG", "count2++: "+count)
                }

                2 ->
                {
                    mSetpview0!!.setStepsViewIndicatorComplectingPosition(list0.size - 1) //设置完成的步数


                    count++
                    sv.post(Runnable { //X,Y are scroll positions untill where you want scroll down
                        sv.scrollTo(300, 300)
                    })
                }
                3 ->{
                    mSetpview0!!.setStepsViewIndicatorComplectingPosition(list0.size - 0) //设置完成的步数

                    count++
                    sv.post(Runnable { //X,Y are scroll positions untill where you want scroll down
                        sv.scrollTo(400, 400)
                    })

                }
                4 ->
                {
                    mSetpview0!!.setStepsViewIndicatorComplectingPosition(list0.size - 0) //设置完成的步数
                    sv.post(Runnable { //X,Y are scroll positions untill where you want scroll down
                        sv.scrollTo(500, 500)
                    })

                }

            }










        })


        btn1.setOnClickListener(View.OnClickListener {

            when (count) {
                4 ->
                {
                    mSetpview0!!.setStepsViewIndicatorComplectingPosition(list0.size - 1) //设置完成的步数

                    count--

                }

                3 ->
                {
                    mSetpview0!!.setStepsViewIndicatorComplectingPosition(list0.size - 2) //设置完成的步数

                    count--

                }
                2 ->
                {
                    mSetpview0!!.setStepsViewIndicatorComplectingPosition(list0.size - 3) //设置完成的步数

                    count--
                    sv.post(Runnable { //X,Y are scroll positions untill where you want scroll down
                        sv.scrollTo(0, 0)
                    })
                }
                1 ->{
                    mSetpview0!!.setStepsViewIndicatorComplectingPosition(list0.size - 4) //设置完成的步数

                    count--
                    Log.d("TAG", "count2--: "+count)

                }
                0 ->
                {
                    mSetpview0!!.setStepsViewIndicatorComplectingPosition(list0.size - 4) //设置完成的步数

//                    count--
//                    Log.d("TAG", "count1--: "+count)
                }


            }
        })

        return view
    }



}