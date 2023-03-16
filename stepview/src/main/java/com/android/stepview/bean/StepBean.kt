package com.android.stepview.bean


class StepBean {
    var name: String? = null
    var state = 0

    constructor() {}
    constructor(name: String?, state: Int) {
        this.name = name
        this.state = state
    }

    companion object {
        const val STEP_UNDO = -1 //未完成  undo step
        const val STEP_CURRENT = 0 //正在进行 current step
        const val STEP_COMPLETED = 1 //已完成 completed step
    }
}