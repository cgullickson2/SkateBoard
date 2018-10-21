package com.example.skateboard;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.skateboard.databinding.GroupThumbnailListBinding;
import com.example.skateboard.databinding.UserThumbnailListBinding;

public class MyAdapter extends RecyclerView.Adapter {

    private ObservableArrayList<Bank> banks;
    private DatabaseRepository databaseRepository;

    public MyAdapter(final ObservableArrayList<Bank> banks, DatabaseRepository databaseRepository) {
        this.banks = banks;
        this.databaseRepository = databaseRepository;
        notifyDataSetChanged();

        banks.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Bank>>() {
            @Override
            public void onChanged(ObservableList<Bank> sender) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<Bank> sender, int positionStart, int itemCount) {
                onChanged(sender);
            }

            @Override
            public void onItemRangeInserted(ObservableList<Bank> sender, int positionStart, int itemCount) {
                onChanged(sender);
            }

            @Override
            public void onItemRangeMoved(ObservableList<Bank> sender, int fromPosition, int toPosition, int itemCount) {
                onChanged(sender);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<Bank> sender, int positionStart, int itemCount) {
                onChanged(sender);
            }
        });
    }

    public void setBanks(ObservableArrayList<Bank> banks) {
        this.banks = banks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        if (banks.get(i).getKey().equals("DUMMY")) {
            return new UserViewHolder((UserThumbnailListBinding) DataBindingUtil.inflate(inflater, R.layout.user_thumbnail_list, viewGroup, false));
        }
        return new MyViewHolder((GroupThumbnailListBinding) DataBindingUtil.inflate(inflater, R.layout.group_thumbnail_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (banks.get(i).getKey().equals("DUMMY")) {
            User user = databaseRepository.getUser().get();
            ((UserViewHolder) viewHolder).bind(user);
        } else {
            try {
                Bank bank = banks.get(i);
                ((MyViewHolder) viewHolder).bind(bank);
            } catch (Exception e) {}
        }
    }

    @Override
    public int getItemCount() {
        return banks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private GroupThumbnailListBinding binding;
        public MyViewHolder(GroupThumbnailListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Bank bank) {
            binding.setBank(bank);
            binding.thumbnailAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseRepository.addFundsToBank(10.0, bank.getKey());
                }
            });
            binding.executePendingBindings();
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private UserThumbnailListBinding binding;
        public UserViewHolder(UserThumbnailListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            final UserThumbnailListBinding bindingReference = binding;
            binding.setUser(databaseRepository.getUser().get());
            databaseRepository.getUser().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    bindingReference.setUser(databaseRepository.getUser().get());
                }
            });
        }

        public void bind(final User user) {
            binding.setUser(user);
            binding.executePendingBindings();
        }
    }
}
