package com.example.docexpiry;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\b\u0010\u0018\u001a\u00020\u0013H\u0016J\u0012\u0010\u0019\u001a\u00020\u00132\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0014J\u0010\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u0007H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\f\u001a\u00020\r8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001e"}, d2 = {"Lcom/example/docexpiry/MainActivity;", "Lcom/example/docexpiry/BaseActivity;", "()V", "adapter", "Lcom/example/docexpiry/CardAdapter;", "allCards", "", "Lcom/example/docexpiry/data/Card;", "binding", "Lcom/example/docexpiry/databinding/ActivityMainBinding;", "notificationManager", "Lcom/example/docexpiry/notifications/NotificationManager;", "viewModel", "Lcom/example/docexpiry/CardListViewModel;", "getViewModel", "()Lcom/example/docexpiry/CardListViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "applyFilters", "", "formatDate", "", "millis", "", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "shareCard", "card", "app_debug"})
public final class MainActivity extends com.example.docexpiry.BaseActivity {
    private com.example.docexpiry.databinding.ActivityMainBinding binding;
    private com.example.docexpiry.CardAdapter adapter;
    private com.example.docexpiry.notifications.NotificationManager notificationManager;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.NotNull
    private java.util.List<com.example.docexpiry.data.Card> allCards;
    
    public MainActivity() {
        super();
    }
    
    private final com.example.docexpiry.CardListViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override
    public void onBackPressed() {
    }
    
    private final void applyFilters() {
    }
    
    private final void shareCard(com.example.docexpiry.data.Card card) {
    }
    
    private final java.lang.String formatDate(long millis) {
        return null;
    }
}