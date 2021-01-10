package xyz.eddief.halfway.ui.main.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialog_home_place_to_meet.view.*
import kotlinx.android.synthetic.main.item_place_type.view.*
import xyz.eddief.halfway.R
import xyz.eddief.halfway.utils.PlaceTypeUtils


class HomePlaceToMeetDialog :
    AppCompatDialogFragment(), PlaceTypesOnClickListener {

    private val listener get() = targetFragment as? PlaceToMeetDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        val rootView: View =
            LayoutInflater.from(activity).inflate(R.layout.dialog_home_place_to_meet, null)
        rootView.apply {
            placeTypes.apply {
                layoutManager = GridLayoutManager(context, 3)
                adapter = PlaceTypesAdapter(
                    PlaceTypeUtils.placeTypesMap.keys.toList(),
                    this@HomePlaceToMeetDialog
                )
            }
            placeToMeetEdit.doAfterTextChanged {
                placeToMeetSubmit.isEnabled = !it.isNullOrBlank()
            }
            placeToMeetSubmit.isEnabled = placeToMeetEdit?.text?.isNotBlank() == true
            placeToMeetSubmit.setOnClickListener {
                onPlaceToMeetEntered(placeToMeetEdit.text.toString())
            }
        }
        val dialog = AlertDialog.Builder(requireActivity())
            .setView(rootView)
            .create()
        return dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            show()
        }
    }

    private fun onPlaceToMeetEntered(placeToMeet: String) {
        listener?.onPlaceToMeetEntered(placeToMeet) ?: displayError()
        dismiss()
    }

    override fun onClick(placeType: String) {
        listener?.onPlaceTypeChosen(placeType) ?: displayError()
        dismiss()
    }

    private fun displayError() = Toast.makeText(
        requireContext(),
        getString(R.string.dialog_place_to_meet_error),
        Toast.LENGTH_LONG
    ).show()
}

interface PlaceToMeetDialogListener {
    fun onPlaceTypeChosen(placeType: String)
    fun onPlaceToMeetEntered(placeToMeet: String)
}

interface PlaceTypesOnClickListener {
    fun onClick(placeType: String)
}

class PlaceTypesAdapter(
    private val placeTypes: List<String>,
    private val onClickListener: PlaceTypesOnClickListener
) :
    RecyclerView.Adapter<PlaceTypesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_place_type, parent, false),
        onClickListener
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindPlaceType(placeTypes[position])

    override fun getItemCount() = placeTypes.size

    class ViewHolder(itemView: View, onClickListener: PlaceTypesOnClickListener) :
        RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onClickListener.onClick(itemView.placeTypeText.text.toString())
            }
        }

        fun bindPlaceType(placeType: String) {
            itemView.placeTypeText.text = placeType
        }
    }
}