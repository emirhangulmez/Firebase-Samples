package com.canerture.firebaseexamples.presentation.firestore.done

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentDoneBinding
import com.canerture.firebaseexamples.presentation.firestore.TodosAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DoneFragment : Fragment(R.layout.fragment_done) {

    private val binding by viewBinding(FragmentDoneBinding::bind)

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            firestoreOperations.getDoneTodosRealtime({ list ->

                TodosAdapter().apply {

                    updateList(list)
                    rvTodos.adapter = this

                    onDoneClick = { state, documentId ->
                        firestoreOperations.updateDoneState(state, documentId, {
                            requireView().showSnack("Done State Updated!")
                        }, {
                            requireView().showSnack(it)
                        })
                    }

                    onEditClick = { documentId ->
                        val action =
                            DoneFragmentDirections.doneToDetail(
                                documentId
                            )
                        findNavController().navigate(action)
                    }

                    onDeleteClick = { documentId ->
                        firestoreOperations.deleteTodo(documentId, {
                            requireView().showSnack("Data deleted!")
                        }, {
                            requireView().showSnack(it)
                        })
                    }
                }
            }, {
                requireView().showSnack(it)
            })
        }
    }
}