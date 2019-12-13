package com.mctech.showcase.feature.heros.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.mctech.showcase.architecture.ComponentState
import com.mctech.showcase.architecture.extention.bindState
import com.mctech.showcase.feature.heros.HeroNavigation
import com.mctech.showcase.feature.heros.HeroViewInteraction
import com.mctech.showcase.feature.heros.HeroViewModel
import com.mctech.showcase.feature.heros.databinding.FragmentListHeroesBinding
import com.mctech.showcase.feature.heros.databinding.ListItemHeroBinding
import com.mctech.showcase.feature.heros.domain.entity.Hero
import com.mctech.showcase.library.design_system.extentions.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HeroListFragment : Fragment() {
    private lateinit var binding: FragmentListHeroesBinding

    private val heroViewModel: HeroViewModel by sharedViewModel()
    private val navigator : HeroNavigation by inject()

    private val loadNextPageScrollMonitor = LoadNextPageScrollMonitor{
        heroViewModel.interact(HeroViewInteraction.LoadNextPage)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        return FragmentListHeroesBinding.inflate(inflater, container, false).also {
            binding = it
            it.viewModel = heroViewModel
            it.lifecycleOwner = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindState(heroViewModel.heroes) { renderScreen(it) }

        binding.listHerosView.addOnScrollListener(loadNextPageScrollMonitor)
        binding.containerError.setOnClickListener{
            heroViewModel.reprocessLastInteraction()
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            heroViewModel.interact(HeroViewInteraction.LoadFirstPage)
        }
    }

    override fun onDestroyView() {
        binding.listHerosView.removeOnScrollListener(loadNextPageScrollMonitor)
        super.onDestroyView()
    }

    private fun renderScreen(state: ComponentState<HeroListState>) {
        hideProgress()

        when (state) {

            is ComponentState.Initializing -> {
                heroViewModel.interact(HeroViewInteraction.LoadFirstPage)
            }

            is ComponentState.Loading -> {
                binding.progressLoading.visibility = if(thereIsNoItemOnList()) View.VISIBLE else View.GONE
                binding.bottomLoadingPager.animateHideByState(thereIsNoItemOnList())
            }

            is ComponentState.Success -> {
                binding.swipeRefreshLayout.visibility = View.VISIBLE
                binding.swipeRefreshLayout.isRefreshing = false

                // Create first list.
                if(thereIsNoItemOnList()){
                    createListOfHeroes(state.result)
                }

                // Update list.
                else {
                    updateListOfHeroes(state.result)
                    scrollListToTop(state.result)
                }
            }
        }
    }

    private fun hideProgress() {
        binding.progressLoading.visibility = View.GONE
        binding.bottomLoadingPager.animateHide()
    }

    private fun createListOfHeroes(result: HeroListState) = createDefaultRecyclerView<Hero, ListItemHeroBinding>(
        recyclerView = binding.listHerosView,
        items = result.heroes,
        viewBindingCreator = { parent, inflater ->
            ListItemHeroBinding.inflate(inflater, parent, false)
        },
        prepareHolder = { item, viewBinding ->
            viewBinding.hero = item
            viewBinding.containerClickable.setOnClickListener {
                lifecycleScope.launch {
                    heroViewModel.interact(HeroViewInteraction.LoadDetails(item))

                    ViewCompat.setTransitionName(viewBinding.tvHeroName, "heroName")
                    ViewCompat.setTransitionName(viewBinding.ivHero, "heroImage")
                    ViewCompat.setTransitionName(viewBinding.containerClickable, "shadow")

                    navigator.navigateToDetails(
                        viewBinding.tvHeroName to "heroName",
                        viewBinding.ivHero to "heroImage",
                        viewBinding.containerClickable to "shadow"
                    )
                }
            }
        }
    )

    private fun updateListOfHeroes(result: HeroListState) = refreshItems(
        recyclerView = binding.listHerosView,
        newItems = result.heroes,
        callback = object : DiffUtil.ItemCallback<Hero>(){
            override fun areItemsTheSame(left: Hero, right: Hero) = left.id == right.id

            override fun areContentsTheSame(left: Hero, right: Hero): Boolean {
                return left.name == right.name
                        && left.countComics == right.countComics
                        && left.countEvents == right.countEvents
                        && left.countSeries == right.countSeries
                        && left.countStories == right.countStories

            }
        }
    )

    private fun scrollListToTop(result: HeroListState) {
        if(result.moveToTop){
            binding.listHerosView.smoothScrollToPosition(0)
        }
    }

    private fun thereIsNoItemOnList(): Boolean {
        return binding.listHerosView.adapter == null
    }
}