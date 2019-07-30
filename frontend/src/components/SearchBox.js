import React, { useEffect } from "react";
import {
  DEFAULT_PAGE_SIZE,
  ACTION_CHANGE_PAGE,
  ACTION_SET_SEARCH_FILTER,
  useAppState
} from "../AppContext";
import FileService from "../services/FileService";
import "./SearchBox.css";

const fileService = new FileService();

function SearchBox() {
  const [{ searchFilter }, dispatch] = useAppState();

  function setSearchFilter(value) {
    dispatch({ type: ACTION_SET_SEARCH_FILTER, value });
  }

  useEffect(() => {
    if (searchFilter != null) {
      fileService.list(0, DEFAULT_PAGE_SIZE, searchFilter).then(response => {
        dispatch({
          type: ACTION_CHANGE_PAGE,
          value: { page: response.page, files: response.files }
        });
      });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [searchFilter]);

  return (
    <div className="SearchBox">
      <input
        onChange={evt => setSearchFilter(evt.target.value)}
        placeholder="Type to filter by name or description ..."
      />
    </div>
  );
}

export default SearchBox;
