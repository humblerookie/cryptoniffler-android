package com.bariski.cryptoniffler.presentation.main

import com.bariski.cryptoniffler.presentation.common.BasePresenter
import com.bariski.cryptoniffler.presentation.main.adapters.GridItemAdapter

interface SearchPresenter : BasePresenter<SearchView>, GridItemAdapter.OnItemClickListener {


}