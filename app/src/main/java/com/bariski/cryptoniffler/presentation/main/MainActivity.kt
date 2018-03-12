package com.bariski.cryptoniffler.presentation.main

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.Fragment
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.presentation.common.BaseActivity
import com.bariski.cryptoniffler.presentation.common.BasePresenter
import com.bariski.cryptoniffler.presentation.common.BaseView
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, MainView {

    @Inject
    lateinit var presenter: MainPresenter

    lateinit var permissionDialog: Dialog


    override val layoutResId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        Fabric.with(this, Crashlytics())
        title = null
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        search.setOnClickListener({ presenter.onSearchClicked() })
        presenter.initView(this, savedInstanceState, intent.extras)


        filter.setOnClickListener({
            val f = fragmentManager.findFragmentById(R.id.container)
            if (f != null && f is View.OnClickListener) {
                f.onClick(it)
            }
        })
    }

    override fun moveToNext(fragment: Fragment, isForward: Boolean) {
        if (isAlive()) {
            if (isForward && fragment is BuyNSellFragment) {
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss()
            } else {
                fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.container, fragment).commitAllowingStateLoss()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onMainViewResumed()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter.saveState(outState)
    }

    override fun onBackPressed() {

        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> {
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            else -> presenter.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        drawer_layout.closeDrawer(GravityCompat.START)
        presenter.onDrawerItemSelected(item.itemId)

        return true
    }


    override fun reviewApp() {
        val uri = Uri.parse("market://details?id=" + packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.releaseView()
    }

    override fun exit() {
        finish()
    }


    override fun moveToDetailsScreen(ignoreFees: Boolean, coin: String?, exchange: String?, amount: Long) {
        val intent = Intent(this, CoinDetailActivity::class.java)
        intent.putExtra("coin", coin)
        intent.putExtra("exchange", exchange)
        intent.putExtra("ignoreFees", ignoreFees)
        intent.putExtra("amount", amount)
        startActivity(intent)
    }

    override fun toggleSearch(visible: Boolean) {
        search.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            presenter.onSearchResult(data?.getParcelableExtra("coin"))
        }
    }

    override fun openSearch() {
        startActivityForResult(Intent(this, SearchActivity::class.java), 1)
    }


    override fun shareApp() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.common_share_text))
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    override fun <T : BaseView> getBasePresenter(): BasePresenter<T> {
        return presenter as BasePresenter<T>
    }

    override fun toggleFilter(b: Boolean) {
        filter.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun toggleProgress(b: Boolean) {
        mainProgress.visibility = if (b) View.VISIBLE else View.GONE
        mainProgress.isClickable = b
        drawer_layout.isEnabled = !b
        drawer_layout.setDrawerLockMode(if (b) DrawerLayout.LOCK_MODE_LOCKED_CLOSED else DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun toggleDrawer(b: Boolean) {
        if (b) {
            drawer_layout.openDrawer(Gravity.LEFT)

        } else {
            drawer_layout.closeDrawer(Gravity.LEFT)
        }
    }

    override fun requestStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog)
            permissionDialog = builder.setTitle(R.string.common_label_permission)
                    .setMessage(R.string.common_permission_storage_rationale)
                    .setPositiveButton(android.R.string.ok, { _, _ ->
                        ActivityCompat.requestPermissions(this,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                1)
                        permissionDialog.dismiss()
                    })
                    .show()
        }
    }

    override fun getCommonPresenter() = presenter
}
