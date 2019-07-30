import React, { createContext, useContext, useReducer } from "react";

const INITIAL_STATE = {
  pageSize: 0,
  totalPages: 0,
  totalElements: 0,
  searchFilter: null,
  page: 0,
  files: []
};

export const ACTION_CHANGE_PAGE = "CHANGE PAGE";
export const ACTION_SET_FILES = "SET FILES";
export const ACTION_RESET = "RESET";
export const ACTION_SET_SEARCH_FILTER = "SET SEARCH FILTER";


function appReducer(state, action) {
  switch (action.type) {
    case ACTION_CHANGE_PAGE:
      const {
        page: { number: page, totalPages, size: pageSize, totalElements },
        files
      } = action.value;
      return { ...state, page, totalPages, pageSize, totalElements, files };
    case ACTION_SET_FILES:
      return { ...state, files: action.value };
    case ACTION_RESET:
      return INITIAL_STATE;
    case ACTION_SET_SEARCH_FILTER:
      return { ...state, searchFilter: action.value };
    default:
      return state;
  }
}

const Context = createContext([ INITIAL_STATE, () => {}]);

function AppContext({ initialState, children }) {
  return (
    <Context.Provider value={useReducer(appReducer, initialState)}>
      {children}
    </Context.Provider>
  );
}

export const useAppState = () => useContext(Context);
export default AppContext;
