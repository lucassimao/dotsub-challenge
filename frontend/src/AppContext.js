import React, { createContext, useContext, useReducer } from "react";

export const DEFAULT_PAGE_SIZE = 5;
export const ACTION_CHANGE_PAGE = "CHANGE PAGE";
export const ACTION_SET_FILES = "SET FILES";
export const ACTION_RESET = "RESET";
export const ACTION_SET_SEARCH_FILTER = "SET SEARCH FILTER";
export const ACTION_HIDE_NOTIFICATION = "HIDE_NOTIFICATION";
export const ACTION_SHOW_ERROR_NOTIFICATION = "SHOW_ERROR_NOTIFICATION";

const INITIAL_STATE = {
  pageSize: DEFAULT_PAGE_SIZE,
  totalPages: 0,
  totalElements: 0,
  searchFilter: undefined,
  notification:null,
  page: 0,
  files: []
};

function appReducer(state, action) {
  switch (action.type) {
    case ACTION_CHANGE_PAGE:
      const {
        page: { number: page, totalPages, size: pageSize, totalElements },
        files
      } = action.value;
      return { ...state, page, totalPages, pageSize, totalElements, files };
    case ACTION_RESET:
      return INITIAL_STATE;
    case ACTION_SET_SEARCH_FILTER:
      return { ...state, searchFilter: action.value };
    case ACTION_SHOW_ERROR_NOTIFICATION:
      return { ...state, notification: {type: 'error', value:action.value} };
    case ACTION_HIDE_NOTIFICATION:
      return { ...state, notification: null };
    default:
      return state;
  }
}

const Context = createContext([INITIAL_STATE, () => {}]);

function AppContext({ children }) {
  return (
    <Context.Provider value={useReducer(appReducer, INITIAL_STATE)}>
      {children}
    </Context.Provider>
  );
}

export const useAppState = () => useContext(Context);
export default AppContext;
