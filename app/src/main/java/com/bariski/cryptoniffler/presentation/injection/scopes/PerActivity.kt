package com.bariski.cryptoniffler.presentation.injection.scopes

import dagger.releasablereferences.CanReleaseReferences
import javax.inject.Scope

@Scope
@CanReleaseReferences
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity