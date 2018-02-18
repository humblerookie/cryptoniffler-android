package com.bariski.cryptoniffler.presentation.common

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.repository.AndroidDataStore
import com.bariski.cryptoniffler.presentation.CryptNifflerApplication
import com.bariski.cryptoniffler.presentation.common.extensions.makeInvisible
import com.bariski.cryptoniffler.presentation.common.extensions.makeVisible
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


abstract class BaseActivity : DaggerAppCompatActivity(), BaseView {
    /**
     * Injection of the form AndroidInjection.inject(this);
     * is done by DaggerAppCompat
     * */
    companion object {
        val EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X"
        val EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y"

    }

    lateinit var rootLayout: View
    abstract val layoutResId: Int
    private var permissionSnackbar: Snackbar? = null
    @Inject
    lateinit var storage: AndroidDataStore

    var revealX: Int = 0
    var revealY: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        rootLayout = findViewById<View>(android.R.id.content)
        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            rootLayout.visibility = View.INVISIBLE

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0)
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0)


            val viewTreeObserver = rootLayout.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        revealActivity(revealX, revealY)
                        rootLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
        } else {
            rootLayout.makeVisible(true)
            if (intent.getIntExtra("entryAnim", 0) != 0 && intent.getIntExtra("exitAnim", 0) != 0) {
                overridePendingTransition(intent.getIntExtra("entryAnim", 0), intent.getIntExtra("exitAnim", 0))
            }
        }
        (applicationContext as CryptNifflerApplication).appComponent.inject(this)

    }

    fun isAlive(): Boolean {
        return !isFinishing && !isDestroyed
    }

    fun requestPermission(permission: String, @StringRes msgId: Int, @StringRes neverAskAgainResId: Int, requestCode: Int?, showSnackbar: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (storage.isRationaleShown(permission)) {
                showRationaleDialog(permission, msgId, requestCode!!)
            } else {
                when {
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode!!)
                    showSnackbar -> showPermissionNeverAskSnackbar(permission, neverAskAgainResId)
                    else -> onPermissionDenied(permission)
                }
            }
        } else {
            onPermissionGranted(permission)
        }
    }

    private fun showPermissionNeverAskSnackbar(permission: String, neverAskAgainResId: Int) {
        val view = findViewById<View>(android.R.id.content)

        if (permissionSnackbar == null) {
            permissionSnackbar = Snackbar
                    .make(view, neverAskAgainResId, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.common_label_permission_settings, {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", packageName, null))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    })
        }
        val snackbar = permissionSnackbar
        snackbar?.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (i in permissions.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permissions[i])
            } else {
                onPermissionDenied(permissions[i])
            }
        }
    }

    protected fun onPermissionGranted(permission: String) {

    }

    protected fun onPermissionDenied(permission: String) {

    }

    protected fun showRationaleDialog(permission: String, @StringRes messageResId: Int, requestCode: Int) {
        storage.setRationaleShown(permission)
        val alertDialog = AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(messageResId).create()
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.common_label_ok)) { dialogInterface, _ -> ActivityCompat.requestPermissions(this@BaseActivity, arrayOf(permission), requestCode) }
        alertDialog.show()


    }

    override fun finish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            unRevealActivity()
        } else {
            super.finish()
        }
    }

    override fun getMessage(resourceId: Int): String {
        return getString(resourceId)
    }


    protected fun revealActivity(x: Int, y: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val finalRadius = (Math.max(rootLayout.width, rootLayout.height) * 1.1).toFloat()

            // create the animator for this view (the start radius is zero)
            val circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0f, finalRadius)
            circularReveal.duration = 400
            circularReveal.interpolator = AccelerateInterpolator()

            // make the view visible and start the animation
            rootLayout.visibility = View.VISIBLE
            circularReveal.start()
        } else {
            finish()
        }
    }

    protected fun unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish()
        } else {
            val finalRadius = (Math.max(rootLayout.width, rootLayout.height) * 1.1).toFloat()
            val circularReveal = ViewAnimationUtils.createCircularReveal(
                    rootLayout, revealX, revealY, finalRadius, 0f)

            circularReveal.duration = 400
            circularReveal.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    rootLayout.makeInvisible()
                    finish()
                }
            })


            circularReveal.start()
        }
    }

}