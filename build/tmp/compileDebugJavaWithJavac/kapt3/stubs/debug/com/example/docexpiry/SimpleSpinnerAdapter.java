package com.example.docexpiry;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0015B\u001b\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0002R\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r\u00a8\u0006\u0016"}, d2 = {"Lcom/example/docexpiry/SimpleSpinnerAdapter;", "Landroid/widget/ArrayAdapter;", "", "context", "Landroid/content/Context;", "items", "", "(Landroid/content/Context;Ljava/util/List;)V", "onItemSelected", "Lcom/example/docexpiry/SimpleSpinnerAdapter$OnItemSelected;", "getOnItemSelected", "()Lcom/example/docexpiry/SimpleSpinnerAdapter$OnItemSelected;", "setOnItemSelected", "(Lcom/example/docexpiry/SimpleSpinnerAdapter$OnItemSelected;)V", "attachListener", "", "spinner", "Landroid/widget/Spinner;", "positionOf", "", "item", "OnItemSelected", "app_debug"})
public final class SimpleSpinnerAdapter extends android.widget.ArrayAdapter<java.lang.String> {
    @org.jetbrains.annotations.Nullable
    private com.example.docexpiry.SimpleSpinnerAdapter.OnItemSelected onItemSelected;
    
    public SimpleSpinnerAdapter(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> items) {
        super(null, 0);
    }
    
    public final int positionOf(@org.jetbrains.annotations.NotNull
    java.lang.String item) {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.example.docexpiry.SimpleSpinnerAdapter.OnItemSelected getOnItemSelected() {
        return null;
    }
    
    public final void setOnItemSelected(@org.jetbrains.annotations.Nullable
    com.example.docexpiry.SimpleSpinnerAdapter.OnItemSelected p0) {
    }
    
    public final void attachListener(@org.jetbrains.annotations.NotNull
    android.widget.Spinner spinner) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\b"}, d2 = {"Lcom/example/docexpiry/SimpleSpinnerAdapter$OnItemSelected;", "", "onSelected", "", "pos", "", "value", "", "app_debug"})
    public static abstract interface OnItemSelected {
        
        public abstract void onSelected(int pos, @org.jetbrains.annotations.NotNull
        java.lang.String value);
    }
}