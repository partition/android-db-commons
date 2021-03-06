package com.getbase.android.db.example.ui;

import com.getbase.android.db.example.content.Contract;
import com.getbase.android.db.example.model.Person;
import com.getbase.android.db.loaders.CursorLoaderBuilder;
import com.google.common.base.Function;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;

import java.util.List;

public class WrapperTransformationListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<SectionedPeopleList> {

  private static final int LOADER_PEOPLE = 0;

  private SectionedPeopleAdapter adapter;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    adapter = new SectionedPeopleAdapter(getActivity());
    setListAdapter(adapter);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    getLoaderManager().initLoader(LOADER_PEOPLE, null, this);
  }

  @Override
  public Loader<SectionedPeopleList> onCreateLoader(int id, Bundle args) {
    return CursorLoaderBuilder.forUri(Contract.People.CONTENT_URI)
        .projection(Contract.People.FIRST_NAME, Contract.People.SECOND_NAME)
        .orderBy(Contract.People.FIRST_NAME)
        .transform(new Function<Cursor, Person>() {
          @Override
          public Person apply(Cursor cursor) {
            final String firstName = cursor.getString(cursor.getColumnIndexOrThrow(Contract.PeopleColumns.FIRST_NAME));
            final String secondName = cursor.getString(cursor.getColumnIndexOrThrow(Contract.PeopleColumns.SECOND_NAME));
            return new Person(firstName, secondName);
          }
        })
        .wrap(new Function<List<Person>, SectionedPeopleList>() {
          @Override
          public SectionedPeopleList apply(List<Person> people) {
            return new SectionedPeopleList(people);
          }
        })
        .build(getActivity());
  }

  @Override
  public void onLoadFinished(Loader<SectionedPeopleList> loader, SectionedPeopleList data) {
    adapter.setNewModel(data);
  }

  @Override
  public void onLoaderReset(Loader<SectionedPeopleList> loader) {
    adapter.setNewModel(null);
  }
}
