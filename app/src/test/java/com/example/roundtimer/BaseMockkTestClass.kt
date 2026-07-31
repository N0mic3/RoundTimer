package com.example.roundtimer

import io.mockk.MockKAnnotations
import org.junit.Before

open class BaseMockkTestClass {

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)
    }
}