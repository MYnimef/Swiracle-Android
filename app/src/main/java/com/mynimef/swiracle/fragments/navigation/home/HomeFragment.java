package com.mynimef.swiracle.fragments.navigation.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mynimef.swiracle.Interfaces.IHome;
import com.mynimef.swiracle.R;
import com.mynimef.swiracle.adapters.HomePostAdapter;
import com.mynimef.swiracle.custom.Fragment;
import com.mynimef.swiracle.dialogs.login.LoginDialogFragment;
import com.mynimef.swiracle.fragments.messenger.MessengerFragment;
import com.mynimef.swiracle.fragments.search.SearchFragment;
import com.mynimef.swiracle.logic.FragmentChanger;
import com.mynimef.swiracle.models.PostInfo;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public final class HomeFragment extends Fragment implements IHome {
    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finishAndRemoveTask();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton search = root.findViewById(R.id.search);
        search.setOnClickListener(v -> FragmentChanger.replaceFragment(
                getParentFragmentManager(),
                R.id.nav_host_fragment,
                new SearchFragment()
        ));

        Button following = root.findViewById(R.id.following);
        Button forYou = root.findViewById(R.id.forYou);

        forYou.setSelected(true);
        following.setOnClickListener(v -> {
            if (homeViewModel.getSignedIn() != 1) {
                new LoginDialogFragment().show(getChildFragmentManager(), "ASK");
            } else {
                forYou.setSelected(false);
                following.setSelected(true);
            }
        });
        forYou.setOnClickListener(v -> {
            following.setSelected(false);
            forYou.setSelected(true);
        });

        ImageButton messenger = root.findViewById(R.id.messages);
        messenger.setOnClickListener(v -> {
            if (homeViewModel.getSignedIn() != 1) {
                new LoginDialogFragment().show(getChildFragmentManager(), "ASK");
            } else {
                FragmentChanger.replaceFragment(
                        requireActivity().getSupportFragmentManager(),
                        R.id.mainFragment,
                        new MessengerFragment(this)
                );
            }
        });

        RecyclerView rv = root.findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        HomePostAdapter adapter = new HomePostAdapter(this, homeViewModel);
        rv.setAdapter(adapter);

        SwipeRefreshLayout swipeRefresh = root.findViewById(R.id.swipeRefreshHome);
        swipeRefresh.setOnRefreshListener(() -> homeViewModel.update());

        homeViewModel.getRecommendationList().observe(getViewLifecycleOwner(), posts -> {
            adapter.setPosts(posts);
            adapter.notifyDataSetChanged();
        });

        homeViewModel.getAnimRefresh().observe(getViewLifecycleOwner(), upd -> {
            swipeRefresh.setRefreshing(!upd);
        });

        homeViewModel.getAnimLayout().observe(getViewLifecycleOwner(), upd -> {
            if (upd) {
                rv.startAnimation(AnimationUtils.loadAnimation(
                        getContext(),
                        R.anim.item_animation_fall_down
                ));
            }
        });

        homeViewModel.getErrorMessage().observe(
                getViewLifecycleOwner(),
                message -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show()
        );

        return root;
    }

    @Override
    public int getSignedIn() { return homeViewModel.getSignedIn(); }
    @Override
    public void likePost(String id) {
        homeViewModel.likePost(id);
    }
    @Override
    public void updatePostInfo(PostInfo postInfo) { homeViewModel.updatePostInfo(postInfo); }

    @Override
    public Fragment getFragment() {
        return this;
    }
}