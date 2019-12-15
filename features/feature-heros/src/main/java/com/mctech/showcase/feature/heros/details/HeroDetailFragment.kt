package com.mctech.showcase.feature.heros.details

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mctech.showcase.architecture.ComponentState
import com.mctech.showcase.architecture.extention.bindState
import com.mctech.showcase.feature.heros.HeroViewInteraction
import com.mctech.showcase.feature.heros.HeroViewModel
import com.mctech.showcase.feature.heros.databinding.FragmentHeroDetaisBinding
import com.mctech.showcase.feature.heros.databinding.ListItemComicBinding
import com.mctech.showcase.feature.heros.domain.entity.Comic
import com.mctech.showcase.library.design_system.extentions.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HeroDetailFragment : Fragment(){
    private lateinit var binding: FragmentHeroDetaisBinding

    private val heroViewModel: HeroViewModel by sharedViewModel()
    private val loadNextPageScrollMonitor = LoadNextPageScrollMonitor{
        heroViewModel.interact(HeroViewInteraction.LoadNextPageOfComics)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        return FragmentHeroDetaisBinding.inflate(inflater, container, false).also {
            binding = it
            it.viewModel = heroViewModel
            it.lifecycleOwner = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindState(heroViewModel.comicHero) { renderScreen(it) }

        binding.containerError.setOnClickListener{
            heroViewModel.reprocessLastInteraction()
        }
        binding.listComics.addOnScrollListener(loadNextPageScrollMonitor)
    }


    override fun onDestroyView() {
        binding.listComics.removeOnScrollListener(loadNextPageScrollMonitor)
        super.onDestroyView()
    }

    private fun renderScreen(state: ComponentState<List<Comic>>) {
        when(state){
            is ComponentState.Success -> {
                // Create first list.
                if(thereIsNoItemOnList()){
                    createListOfComics(state.result)
                }

                // Update list.
                else {
                    updateListOfComics(state.result)
                }
            }
        }
    }

    private fun createListOfComics(result: List<Comic>) = createDefaultRecyclerView<Comic, ListItemComicBinding>(
        recyclerView = binding.listComics,
        items = result,
        layoutOrientation = RecyclerView.HORIZONTAL,
        viewBindingCreator = { parent, inflater ->
            ListItemComicBinding.inflate(inflater, parent, false)
        },
        prepareHolder = { item, viewBinding ->
            viewBinding.comic = item
        }
    )

    private fun updateListOfComics(result: List<Comic>)  = refreshItems(
        recyclerView = binding.listComics,
        newItems = result,
        callback = object : DiffUtil.ItemCallback<Comic>(){
            override fun areItemsTheSame(left: Comic, right: Comic) = left.thumbnail == right.thumbnail
            override fun areContentsTheSame(left: Comic, right: Comic) = left.thumbnail == right.thumbnail
        }
    )

    private fun thereIsNoItemOnList(): Boolean {
        return binding.listComics.adapter == null
    }
}