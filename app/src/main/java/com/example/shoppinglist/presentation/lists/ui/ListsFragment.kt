package com.example.shoppinglist.presentation.lists.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentListsBinding
import com.example.shoppinglist.domain.models.ShoppingList
import com.example.shoppinglist.presentation.lists.models.CreatedState
import com.example.shoppinglist.presentation.lists.models.ScreenState
import com.example.shoppinglist.presentation.lists.view_model.ListsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListsFragment : Fragment() {
    private var _binding: FragmentListsBinding? = null
    private val binding get() = _binding!!
    private lateinit var listsAdapter: ListsAdapter
    private val viewModel: ListsViewModel by viewModel()
    private lateinit var bottomSheetBehaviorLists: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        with(viewModel) {
            getAllLists()

            createdState.observe(viewLifecycleOwner) { state ->
                reactOnListCreation(state)
            }

            screenState.observe(viewLifecycleOwner) { state ->
                manageScreenContent(state)
            }

            shoppingLists.observe(viewLifecycleOwner) { newList ->
                listsAdapter.submitList(newList)
            }

            numberOfProducts.observe(viewLifecycleOwner) { numberOfProducts ->
                binding.listItemsNumber.text = numberOfProducts
            }
        }


        with(binding) {
            creteNewListButton.setOnClickListener { showCreationDialog() }
            deleteButton.setOnClickListener { showListDeletingDialog() }
        }


        bottomSheetBehaviorLists = BottomSheetBehavior.from(binding.bottomSheetLayoutLists).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
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
                    creteNewListButton.visibility = View.VISIBLE
                }

                ScreenState.Error -> {
                    recyclerView.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    introLayout.visibility = View.GONE
                    creteNewListButton.visibility = View.GONE
                }

                ScreenState.Loading -> {
                    recyclerView.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    introLayout.visibility = View.GONE
                    creteNewListButton.visibility = View.GONE
                }

                ScreenState.Intro -> {
                    recyclerView.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    introLayout.visibility = View.VISIBLE
                    creteNewListButton.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun showCreationDialog() {

        val dialogView = View.inflate(requireContext(), R.layout.create_dialog, null)

        createEmptyFieldTextWatcher(dialogView)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.new_list))
            .setView(dialogView)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.negative_button)) { _, _ -> }
            .setPositiveButton(getString(R.string.positive_button)) { _, _ ->

                val dialogEditText: TextView = dialogView.findViewById(R.id.edit_list_name)

                if (dialogEditText.text.isBlank()) {
                    Snackbar.make(
                        binding.listsConstraintView,
                        R.string.name_must_be_not_blank,
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(R.string.ok) {}
                        .setActionTextColor(resources.getColor(R.color.white, null))
                        .show()
                } else {
                    viewModel.createList(dialogEditText.text.toString())
                }

            }.show()
    }

    private fun showListDeletingDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_dialog_title))
            .setMessage(getString(R.string.delete_dialog_message))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.negative_button)) { _, _ -> }
            .setPositiveButton(getString(R.string.delete)) { _, _ ->

                viewModel.deleteShoppingList()
                bottomSheetBehaviorLists.state = BottomSheetBehavior.STATE_HIDDEN

                Snackbar.make(
                    binding.listsConstraintView,
                    R.string.restore_message,
                    RESTORE_WAITING_TIME
                )
                    .setActionTextColor(resources.getColor(R.color.white, null))
                    .setAction(R.string.yes) { viewModel.restoreShoppingList() }
                    .show()

            }.show()
    }

    private fun setRecyclerView() {

        listsAdapter = ListsAdapter(requireContext(), onItemClickListener = { shoppingList ->

            val action = ListsFragmentDirections.actionListsFragmentToDetailsFragment(
                shoppingList.id,
                shoppingList.name
            )
            findNavController().navigate(action)
        },

            onItemLongClickListener = { shoppingList ->
                viewModel.getShoppingListItemsQuantity(shoppingList.id)
                bindBottomSheetViews(shoppingList)
                bottomSheetBehaviorLists.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        )

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        binding.recyclerView.adapter = listsAdapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun reactOnListCreation(createdState: CreatedState) {
        when (createdState) {
            is CreatedState.Created -> showCreationSnackbar(createdState)
            CreatedState.NotCreated -> showCreationSnackbar(createdState)
            CreatedState.Default -> {}
        }
    }

    private fun showCreationSnackbar(createdState: CreatedState) {

        when (createdState) {
            is CreatedState.Created -> Snackbar.make(
                binding.listsConstraintView,
                getString(R.string.success_creation, createdState.listName),
                Snackbar.LENGTH_SHORT
            ).show()

            CreatedState.NotCreated -> Snackbar.make(
                binding.listsConstraintView,
                R.string.not_success_creation,
                Snackbar.LENGTH_SHORT
            ).show()

            CreatedState.Default -> {}
        }
    }

    private fun manageBottomSheetShadowAndFab() {
        bottomSheetBehaviorLists.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                        binding.creteNewListButton.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                        binding.creteNewListButton.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })
    }

    private fun createEmptyFieldTextWatcher(view: View) {

        val editText: TextView = view.findViewById(R.id.edit_list_name)
        val inputLayout: TextInputLayout = view.findViewById(R.id.input_list_name)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    inputLayout.error = getString(R.string.edit_error_message)
                } else inputLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun bindBottomSheetViews(shoppingList: ShoppingList) {
        binding.listName.text = shoppingList.name
        binding.listCreatedDate.text = getString(R.string.list_created_date, shoppingList.created)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("LLL", "onDestroyView")
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.allowPooling()
        viewModel.startPooling()
        Log.d("LLL", "onResume")

    }

    override fun onPause() {
        super.onPause()
        viewModel.stopPooling()
        Log.d("LLL", "onPause")
    }

    companion object {
        const val RESTORE_WAITING_TIME = 5000
    }

}