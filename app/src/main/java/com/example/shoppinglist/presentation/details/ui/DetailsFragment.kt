package com.example.shoppinglist.presentation.details.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentDetailsBinding
import com.example.shoppinglist.presentation.details.models.AddedState
import com.example.shoppinglist.presentation.details.view_model.DetailsViewModel
import com.example.shoppinglist.presentation.lists.models.ScreenState
import com.example.shoppinglist.presentation.lists.ui.ListsFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private lateinit var bottomSheetBehaviorProducts: BottomSheetBehavior<LinearLayout>
    private val viewModel: DetailsViewModel by viewModel()
    private var listName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.getListId(it.getInt(LIST_ID))
            listName = it.getString(LIST_NAME,"")
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigateBack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        bindViews()


        with(viewModel){
            getShoppingList()
            listOfProducts.observe(viewLifecycleOwner) { listOfProducts ->
                productAdapter.submitList(listOfProducts)
            }

            screenState.observe(viewLifecycleOwner){screenState->
                manageScreenContent(screenState)
            }
            addedState.observe(viewLifecycleOwner){addedState->
                reactOnProductAdding(addedState)
            }
        }


        with(binding){
            cancelDeletingButton.setOnClickListener {
                bottomSheetBehaviorProducts.state = BottomSheetBehavior.STATE_HIDDEN
            }

            deleteProductButton.setOnClickListener { showProductDeletingDialog() }

            topAppBar.setNavigationOnClickListener {
                navigateBack()
            }

            creteNewProductButton.setOnClickListener {
                showAddingDialog()
            }
        }


        bottomSheetBehaviorProducts =
            BottomSheetBehavior.from(binding.bottomSheetLayoutProducts).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
                isDraggable = false
            }

        manageBottomSheetShadowAndFab()
    }

    private fun manageScreenContent(screenState: ScreenState) {
        with(binding) {
            when (screenState) {
                ScreenState.Content -> {
                    recyclerView.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    introLayout.visibility = View.GONE
                    creteNewProductButton.visibility = View.VISIBLE
                }

                ScreenState.Error -> {
                    recyclerView.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    introLayout.visibility = View.GONE
                    creteNewProductButton.visibility = View.GONE
                }

                ScreenState.Loading -> {
                    recyclerView.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    introLayout.visibility = View.GONE
                    creteNewProductButton.visibility = View.GONE
                }

                ScreenState.Intro -> {
                    recyclerView.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    introLayout.visibility = View.VISIBLE
                    creteNewProductButton.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun manageBottomSheetShadowAndFab() {
        bottomSheetBehaviorProducts.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                        binding.creteNewProductButton.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.overlay.visibility = View.GONE
                        binding.creteNewProductButton.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })
    }

    private fun setRecyclerView() {
        productAdapter = ProductAdapter(
            onItemLongClickListener = { product ->
                viewModel.getItemId(product.id)
                bottomSheetBehaviorProducts.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        )

        binding.recyclerView.adapter = productAdapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun reactOnProductAdding(addedState: AddedState) {
        when (addedState) {
            is AddedState.Added -> {
                showCreationSnackbar(addedState)
            }

            AddedState.Default -> {
                showCreationSnackbar(addedState)
            }

            AddedState.NotAdded -> {}
        }
    }

    private fun showCreationSnackbar(addedState: AddedState) {

        when (addedState) {

            is AddedState.Added -> Snackbar.make(
                binding.productsConstraintView,
                getString(R.string.success_adding, addedState.productName),
                Snackbar.LENGTH_SHORT
            ).show()

            AddedState.NotAdded -> Snackbar.make(
                binding.productsConstraintView,
                R.string.not_success_adding,
                Snackbar.LENGTH_SHORT
            ).show()

            AddedState.Default -> {}
        }
    }

    private fun bindViews() {
        binding.topAppBar.title = listName
    }

    private fun showAddingDialog() {

        val dialogView = View.inflate(requireContext(), R.layout.add_dialog, null)

        createEmptyFieldTextWatcher(dialogView)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.add_product))
            .setView(dialogView)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.negative_button)) { _, _ -> }
            .setPositiveButton(getString(R.string.positive_button)) { _, _ ->

                val dialogEditTextName: TextView = dialogView.findViewById(R.id.edit_product_name)
                val dialogEditTextQuantity: TextView = dialogView.findViewById(R.id.edit_quantity)

                if (dialogEditTextName.text.isBlank() || dialogEditTextQuantity.text.isBlank()) {
                    Snackbar.make(
                        binding.productsConstraintView,
                        R.string.fields_must_not_be_blank,
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(R.string.ok) {}
                        .setActionTextColor(resources.getColor(R.color.white, null))
                        .show()
                } else {
                    viewModel.addItemToShoppingList(
                        dialogEditTextName.text.toString(),
                        dialogEditTextQuantity.text.toString().toInt()
                    )
                }
            }.show()
    }

    private fun createEmptyFieldTextWatcher(view: View) {

        val editTextName: TextView = view.findViewById(R.id.edit_product_name)
        val inputLayoutName: TextInputLayout = view.findViewById(R.id.input_product_name)

        val editTextQuantity: TextView = view.findViewById(R.id.edit_quantity)
        val inputLayoutQuantity: TextInputLayout = view.findViewById(R.id.input_quantity)


        editTextQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    inputLayoutQuantity.error = getString(R.string.edit_error_message)
                } else inputLayoutQuantity.error = null
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    inputLayoutName.error = getString(R.string.edit_error_message)
                } else inputLayoutName.error = null
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showProductDeletingDialog() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_dialog_product))
            .setMessage(getString(R.string.delete_product__dialog_message))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.negative_button)) { _, _ -> }
            .setPositiveButton(getString(R.string.delete)) { _, _ ->

                viewModel.deleteItemFromShoppingList()
                bottomSheetBehaviorProducts.state = BottomSheetBehavior.STATE_HIDDEN

            }.show()
    }


    private fun navigateBack(){
        val action = DetailsFragmentDirections.actionDetailsFragmentToListsFragment()
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        viewModel.allowPooling()
        viewModel.startPooling()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopPooling()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val LIST_ID = "listId"
        const val LIST_NAME = "listName"
    }
}