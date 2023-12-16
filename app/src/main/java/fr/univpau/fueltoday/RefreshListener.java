package fr.univpau.fueltoday;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
    private MainActivity activity;

    public RefreshListener(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onRefresh() {
        this.activity.updateLocation();
        new StationRPC(this.activity).execute();
    }

    public void onTaskCompleted() {
        this.activity.swipeRefreshLayout.setRefreshing(false);
    }
}
