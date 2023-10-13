package com.example.grocerylist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.grocerylist.dataViewModel.GroceryViewModel
import com.example.grocerylist.dataViewModel.GroceryViewModelFactory
import com.example.grocerylist.databinding.FragmentGroceryAddBinding

class GroceryAddFragment : Fragment() {

    companion object {
        const val TYPE = "type"
    }

    private lateinit var typeId: String

    // binding
    private lateinit var binding: FragmentGroceryAddBinding

    // widgets
    private lateinit var inputGrocery: com.google.android.material.textfield.TextInputEditText
    private lateinit var inputQty: com.google.android.material.textfield.TextInputEditText
    private lateinit var btnAdd: Button

    // viewModel initialization
    private val viewModel: GroceryViewModel by viewModels {
        GroceryViewModelFactory(
            (activity?.application as GroceryApplication).database.groceryDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            typeId = it.getString(TYPE).toString()
        }
        Log.d("ImageButton Chosen", typeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroceryAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputGrocery = binding.inputGrocery
        inputQty = binding.inputQty
        btnAdd = binding.btnAdd
        btnAdd.setOnClickListener { btnClicked(typeId) }
    }

    private fun btnClicked(type: String) {
        val name = inputGrocery.text.toString()
        val qty = inputQty.text.toString()

        // check if entry is filled
        if (qty.isNotEmpty() && viewModel.isQuantityNotZero(qty)) {
            if (viewModel.isEntryValid(
                    0,
                    inputGrocery.text.toString(),
                    inputQty.text.toString()
                )
            ) {
                // call DAO to insert grocery
                viewModel.addNewGrocery(
                    (when (type) {
                        "Raw" -> 1
                        "Clean" -> 2
                        "Water" -> 3
                        "Snack" -> 4
                        "Fruit or Veggies" -> 5
                        else -> 6
                    }),
                    name,
                    qty.toInt()
                )
                val action = GroceryAddFragmentDirections.actionGroceryAddFragmentToGroceryList()
                this.findNavController().navigate(action)
            } else {
                Toast.makeText(context, "Fill out the empty fields !", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Quantity cannot be Zero or Null!", Toast.LENGTH_SHORT).show()
        }
    }
}