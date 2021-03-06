package com.getbase.android.db.example.ui;

import com.getbase.android.db.example.content.Contract;
import com.getbase.android.db.loaders.CursorLoaderBuilder;
import com.google.common.base.Function;

import android.R;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

public class SimpleTransformedStringsListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<String>> {

  private static final int LOADER_ID = 0;
  private ArrayAdapter<String> mAdapter;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_1, R.id.text1);
    setListAdapter(mAdapter);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    getLoaderManager().initLoader(LOADER_ID, null, this);
  }

  @Override
  public Loader<List<String>> onCreateLoader(int id, Bundle args) {
    return CursorLoaderBuilder.forUri(Contract.People.CONTENT_URI)
        .projection(Contract.People.FIRST_NAME, Contract.People.SECOND_NAME)
        .transform(new Function<Cursor, String>() {
          @Override
          public String apply(Cursor cursor) {
            return String.format("%s %s", cursor.getString(0), cursor.getString(1));
          }
        })
        .build(getActivity());
  }

  @Override
  public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
    mAdapter.addAll(data);
  }

  @Override
  public void onLoaderReset(Loader<List<String>> loader) {
    mAdapter.clear();
  }
}
