package xyz.eddief.halfway.ui.main.home

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import xyz.eddief.halfway.R
import xyz.eddief.halfway.utils.toPX

class HomeProfileView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val imageView = AppCompatImageView(context)
    private val addressView = AppCompatTextView(context)

    init {
        val contentPadding = 6f.toPX(context)
        setContentPadding(contentPadding, contentPadding, contentPadding, contentPadding)
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.button_default))

        addView(
            LinearLayoutCompat(context).apply {
                orientation = LinearLayoutCompat.VERTICAL
                gravity = Gravity.CENTER_HORIZONTAL

                addView(
                    imageView.apply {
                        alpha = .5f
                    },
                    LinearLayoutCompat.LayoutParams(
                        50f.toPX(context),
                        50f.toPX(context)
                    )
                )
                addView(
                    addressView.apply {
                        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10f)
                    },
                    LinearLayoutCompat.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                )
            },
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )
    }

    fun setProfile(isSet: Boolean, addressText: String?) {
        setProfileImage(isSet)
        addressText?.let {
            addressView.text = it
        }
    }

    fun test(locationsAmount: Int, amount: Int) {
        isVisible = locationsAmount > amount
        setProfileImage(locationsAmount > amount + 1)
    }

    fun setProfileImage(isSet: Boolean) {
        imageView.setImageResource(
            if (isSet) R.drawable.ic_person_24 else R.drawable.ic_person_add_24
        )
    }
}