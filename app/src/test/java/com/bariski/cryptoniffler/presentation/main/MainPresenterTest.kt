package com.bariski.cryptoniffler.presentation.main

import android.graphics.Bitmap
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.domain.model.Exchange
import com.bariski.cryptoniffler.domain.repository.DeviceDataStore
import com.bariski.cryptoniffler.domain.repository.EventsRepository
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.domain.util.Screen
import com.bariski.cryptoniffler.inject.data.DeviceDataStoreMockImpl
import com.bariski.cryptoniffler.inject.data.EventsRepositoryMockImpl
import com.bariski.cryptoniffler.inject.data.NifflerRepositoryMockImpl
import com.bariski.cryptoniffler.inject.data.SchedulersMock
import com.bariski.cryptoniffler.presentation.main.adapters.GridItemAdapter
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.times
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.io.File


@RunWith(JUnit4::class)
class MainPresenterTest {

    @Mock
    lateinit var view: MainView

    @Mock
    lateinit var imageLoader: ImageLoader

    @Mock
    lateinit var analytics: Analytics

    @Mock
    lateinit var bitmap: Bitmap

    lateinit var nifflerRepository: NifflerRepository
    lateinit var eventsRepository: EventsRepository
    lateinit var deviceDataStore: DeviceDataStore
    lateinit var mainPresenter: MainPresenterImpl


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        nifflerRepository = NifflerRepositoryMockImpl()
        eventsRepository = EventsRepositoryMockImpl()
        deviceDataStore = DeviceDataStoreMockImpl()
        mainPresenter = MainPresenterImpl(nifflerRepository, eventsRepository, deviceDataStore,
                GridItemAdapter(imageLoader), SchedulersMock(), analytics, imageLoader)
        mainPresenter.initView(view, null, null)

    }


    @Test
    fun `Test coin success receiver navigates properly`() {
        val list = ArrayList<Coin>()
        mainPresenter
                .getNavigateToCoinSelectionSuccessListener(false)
                .invoke(list)
        verify(view).toggleProgress(false)
        verify(view).toggleSearch(true)
        verify(view).moveToNext(any(), eq(false))
    }

    @Test
    fun `Test coin success receiver calls analytics properly`() {
        val list = ArrayList<Coin>()
        mainPresenter
                .getNavigateToCoinSelectionSuccessListener(false)
                .invoke(list)
        verify(analytics).sendScreenView(eq(Screen.PICK_COIN))
    }

    @Test
    fun `Test exchange success receiver navigates properly `() {
        val list = ArrayList<Exchange>()
        mainPresenter
                .getNavigateToExchangeSelectionSuccessReceiver(false)
                .invoke(list)
        verify(view).toggleProgress(false)
        verify(view).toggleSearch(false)
        verify(view).moveToNext(any(), eq(false))
    }

    @Test
    fun `Test exchange success receiver calls analytics properly`() {
        val list = ArrayList<Exchange>()
        mainPresenter
                .getNavigateToExchangeSelectionSuccessReceiver(false)
                .invoke(list)
        verify(analytics).sendScreenView(eq(Screen.PICK_EXCHANGE))
    }

    @Test
    fun `Test info click logs analytics and shows dialog`() {
        mainPresenter.infoClicked()
        verify(view).showInfo()
        verify(analytics).logInfoClick(false)
    }

    @Test
    fun `Test when share item selected shares app`() {
        mainPresenter.onDrawerItemSelected(R.id.share)
        verify(view).shareApp()
    }

    @Test
    fun `Test when calendar item selected moves to calendar section and changes menu option`() {
        mainPresenter.onDrawerItemSelected(R.id.calendar)
        verify(view).toggleSearch(false)
        verify(view).toggleInfo(false)
        verify(view).toggleFilter(true)
        verify(view, times(2)).moveToNext(any(), eq(true))
    }


    @Test
    fun `Test when share screen item selected shares image`() {
        Mockito.`when`(view.requestStoragePermission(true)).then {
            mainPresenter.onStorageGranted()
        }
        val file = File("demo", "demo")
        Mockito.`when`(view.screenShot).thenReturn(bitmap)
        Mockito.`when`(imageLoader.saveScreenshot(bitmap)).thenReturn(file)
        mainPresenter.onDrawerItemSelected(R.id.shareScreen)
        verify(view).shareArbitrage(file)

    }

    @Test
    fun `Test when report item selected moves to home section and changes menu option`() {
        mainPresenter.onDrawerItemSelected(R.id.report)
        verify(view).sendFeedback()
    }


    @Test
    fun `Test when home item selected moves to home section and changes menu option`() {
        mainPresenter.onDrawerItemSelected(R.id.home)
        verify(view).toggleSearch(false)
        verify(view).toggleInfo(false)
        verify(view).toggleFilter(false)
        verify(view, times(2)).moveToNext(any(), eq(true))
    }

}