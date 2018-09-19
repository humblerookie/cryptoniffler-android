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
import android.support.v4.content.FileProvider
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.presentation.common.BaseActivity
import com.bariski.cryptoniffler.presentation.common.BasePresenter
import com.bariski.cryptoniffler.presentation.common.BaseView
import com.bariski.cryptoniffler.presentation.common.listeners.ItemIdClickListener
import com.bariski.cryptoniffler.presentation.common.utils.FEEDBACK_EMAIL
import com.bariski.cryptoniffler.presentation.common.utils.PLAY_STORE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.File
import javax.inject.Inject


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, MainView, View.OnClickListener, ItemIdClickListener {


    @Inject
    lateinit var presenter: MainPresenter

    private lateinit var permissionDialog: Dialog


    override val layoutResId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        title = null
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        search.setOnClickListener { presenter.onSearchClicked() }
        presenter.initView(this, savedInstanceState, intent.extras)


        filter.setOnClickListener {
            val f = fragmentManager.findFragmentById(R.id.container)
            if (f != null && f is View.OnClickListener) {
                f.onClick(it)
            }
        }
        info.setOnClickListener {
            val f = fragmentManager.findFragmentById(R.id.container)
            if (f != null && f is View.OnClickListener) {
                f.onClick(it)
            }
        }
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
        val uri = Uri.parse("market://details?id=$packageName")
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
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
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
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.common_share_text) + PLAY_STORE)
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    override fun sendFeedback() {
        if (isAlive()) {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:") // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(FEEDBACK_EMAIL))
            intent.putExtra(Intent.EXTRA_SUBJECT, "")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
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

    override fun requestStoragePermission(ignoreDialog: Boolean) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && !ignoreDialog) {
            val builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog)
            permissionDialog = builder.setTitle(R.string.common_label_permission)
                    .setMessage(R.string.common_permission_storage_rationale)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        ActivityCompat.requestPermissions(this,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                1)
                        permissionDialog.dismiss()
                    }
                    .show()
        } else if (ignoreDialog) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1)
        }
    }

    override fun toggleInfo(b: Boolean) {
        info.visibility = if (b) View.VISIBLE else View.GONE
        toggleFilter(b)
    }

    override fun onClick(v: View) {
        onItemClick(v.id)
    }

    override fun onItemClick(id: Int) {
        presenter.onDrawerItemSelected(id)
    }


    override fun shareArbitrage(file: File) {
        val uri = FileProvider.getUriForFile(this, this.applicationContext.packageName + ".provider", file)
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "Arbitrage opportunity on Crypto Niffler$PLAY_STORE");
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getCommonPresenter() = presenter

    override fun onPermissionGranted(permission: String) {
        super.onPermissionGranted(permission)
        presenter.onStorageGranted()
    }

    override fun onPermissionDenied(permission: String) {
        super.onPermissionDenied(permission)
        presenter.onStorageFailed()
    }

    override fun showInfo() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
        builder.setTitle(getString(R.string.common_label_fee_info))
                .setMessage(getString(R.string.common_info_fee_info))
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    override fun showVersionChangeInfo() {
        MaterialDialog.Builder(this)
                .title(R.string.changed_title)
                .content(getString(R.string.changed_content))
                .positiveText(R.string.common_label_ok)
                .iconRes(R.drawable.ic_volume)
                .onPositive { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }
}
