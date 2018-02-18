package com.bariski.cryptoniffler.presentation.main

import com.bariski.cryptoniffler.domain.model.Device
import com.bariski.cryptoniffler.domain.model.ResponseWrapper
import com.bariski.cryptoniffler.domain.repository.DeviceRepository
import com.bariski.cryptoniffler.domain.repository.UserRepository
import com.bariski.cryptoniffler.domain.util.SUCCESS
import com.bariski.cryptoniffler.presentation.common.listeners.FcmRegistrar
import com.bariski.cryptoniffler.presentation.home.MainActivity
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class MainPresenterTest {

    @Mock
    lateinit var view: MainView
    @Mock
    lateinit var devRepo: DeviceRepository
    @Mock
    lateinit var userRepo: UserRepository
    @Mock
    lateinit var registrar: FcmRegistrar

    lateinit var presenter: MainPresenterImpl

    val sc = object : com.bariski.cryptoniffler.domain.common.Schedulers {
        override fun io(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun ui(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun compute(): Scheduler {
            return Schedulers.trampoline()
        }

    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = MainPresenterImpl(view, devRepo, userRepo, sc, registrar)
        Mockito.`when`(devRepo.getFcmToken()).thenReturn("fcmtoken")
        Mockito.`when`(devRepo.getInstanceId()).thenReturn("instanceid")
    }


    @Test
    fun invokingNavigationReturnsLoginclass_whenNotAuthenticated() {
        Mockito.`when`(userRepo.isUserAuthenticated()).thenReturn(false)
        presenter.invokeNavigation()
        Mockito.verify(view).navigateToNext(LoginActivity::class.java)

    }

    @Test
    fun invokingNavigationReturnsMainclass_whenAuthenticated() {
        Mockito.`when`(userRepo.isUserAuthenticated()).thenReturn(true)
        presenter.invokeNavigation()
        Mockito.verify(view).navigateToNext(MainActivity::class.java)

    }


    @Test
    fun startingPresenterRegistersListener_whenNotRegisteredAndTokenAbsent() {
        Mockito.`when`(devRepo.getFcmToken()).thenReturn(null)
        val dev = Device(instanceId = "instanceid", fcmToken = "fcmtoken")
        Mockito.`when`(devRepo.registerDevice(dev)).thenReturn(Single.just(ResponseWrapper(code = SUCCESS)))
        presenter.onStarted()
        Mockito.verify(registrar).setListener(presenter)
    }

    @Test
    fun onTokenInitComplete_CallsRegisterDevice() {
        val dev = Device(instanceId = "instanceid", fcmToken = "fcmtoken")
        Mockito.`when`(devRepo.registerDevice(dev)).thenReturn(Single.just(ResponseWrapper(code = SUCCESS)))
        presenter.onInitializationComplete()
        Mockito.verify(devRepo).registerDevice(dev)
    }

    @Test
    fun onTokenInitFailure_displaysError() {
        presenter.onInitializationFailure()
        Mockito.verify(view).displayRegistrationError()
    }

    @Test
    fun startingPresenterRegistersDevice_whenNotRegisteredAndTokenPresent() {
        Mockito.`when`(devRepo.isDeviceRegistered()).thenReturn(false)
        Mockito.`when`(userRepo.isUserAuthenticated()).thenReturn(false)
        val dev = Device(instanceId = "instanceid", fcmToken = "fcmtoken")
        Mockito.`when`(devRepo.registerDevice(dev)).thenReturn(Single.just(ResponseWrapper(code = SUCCESS)))
        presenter.onStarted()
        Mockito.verify(devRepo).registerDevice(dev)
        Mockito.verify(devRepo).storeDeviceRegistered(true)
        Mockito.verify(view).navigateToNext(LoginActivity::class.java)
    }

    //Todo figure how to test handler on junit
    /*@Test
    fun startingPresenterInvokesNavigation_whenRegisteredAndTokenPresent() {
        Mockito.`when`(devRepo.isDeviceRegistered()).thenReturn(true)
        Mockito.`when`(userRepo.isUserAuthenticated()).thenReturn(true)
        presenter.onStarted()
        Mockito.verify(view).navigateToNext(MainActivity::class.java)
    }*/

    @Test
    fun onRegisterFailure_ShowsError() {
        Mockito.`when`(devRepo.isDeviceRegistered()).thenReturn(false)
        Mockito.`when`(userRepo.isUserAuthenticated()).thenReturn(false)
        val dev = Device(instanceId = "instanceid", fcmToken = "fcmtoken")
        Mockito.`when`(devRepo.registerDevice(dev)).thenReturn(Single.just("abc").map {
            it.substring(0, 12) //throws an exception
            ResponseWrapper(code = SUCCESS)
        })
        presenter.onStarted()
        Mockito.verify(view).displayRegistrationError()
    }

    @After
    fun tearDown() {

    }
}
