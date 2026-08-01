package com.example.roundtimer

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before

open class BaseMockkTestClass {

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    open fun tearDown() {
        unmockkAll()
    }
}