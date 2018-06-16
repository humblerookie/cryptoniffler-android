package com.bariski.cryptoniffler.presentation.main.inject

import android.content.ClipboardManager
import android.content.Context
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.presentation.injection.scopes.PerActivity
import com.bariski.cryptoniffler.presentation.injection.scopes.PerFragment
import com.bariski.cryptoniffler.presentation.main.InfoPresenter
import com.bariski.cryptoniffler.presentation.main.InfoPresenterImpl
import dagger.Module
import dagger.Provides

@Module
@PerActivity
class InfoModule {
    @Provides
    @PerFragment
    fun providesPresenter(clipboardManager: ClipboardManager, repository: NifflerRepository, schedulers: Schedulers, analytics: Analytics): InfoPresenter {
        return InfoPresenterImpl(clipboardManager, repository, schedulers, analytics)
    }

    @Provides
    @PerFragment
    fun providesClipboardManager(context: Context): ClipboardManager {
        return context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
}