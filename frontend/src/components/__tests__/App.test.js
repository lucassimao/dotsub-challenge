import React from "react";
import ReactDOM from "react-dom";
import { act } from "react-dom/test-utils";
import App from "../App";
import { DEFAULT_PAGE_SIZE } from "../../AppContext";

import FileService, { mockFileServiceList } from "../../services/FileService";

jest.mock("../../services/FileService");

let container, modalRoot;

beforeEach(() => {
  jest.useFakeTimers();
});

beforeEach(() => {
  container = document.createElement("div");
  modalRoot = document.createElement("div");
  modalRoot.setAttribute("id", "modal-root");

  document.body.appendChild(container);
  document.body.appendChild(modalRoot);

  FileService.mockClear();
  mockFileServiceList.mockClear();
});
afterEach(() => {
  ReactDOM.unmountComponentAtNode(container);

  document.body.removeChild(modalRoot);
  document.body.removeChild(container);

  container = null;
  modalRoot = null;
});

it("renders without crashing", async () => {
  act(() => {
    ReactDOM.render(<App />, container);
  });

  await act(async () => {
    jest.runAllTimers();
  });

  const searchBox = container.getElementsByClassName("SearchBox")[0];
  expect(searchBox).not.toBeUndefined();

  const table = container.getElementsByTagName("table")[0];
  expect(table).not.toBeUndefined();

  const paginator = container.getElementsByClassName("Paginator")[0];
  expect(paginator).not.toBeUndefined();
});

it("loads the initial page of data, showing in the table and configures the paginator", async () => {
  act(() => {
    ReactDOM.render(<App />, container);
  });

  await act(async () => {
    jest.runAllTimers();
  });

  expect(mockFileServiceList).toHaveBeenCalledWith(
    0,
    DEFAULT_PAGE_SIZE,
    undefined
  );

  const tableRows = container.querySelectorAll("table tbody tr");
  // table must have as much rows as the default paging size
  expect(tableRows.length).toBe(DEFAULT_PAGE_SIZE);

  const paginatorPageSizeInfo = container.querySelector("#total-pages");
  expect(paginatorPageSizeInfo.textContent.trim()).toBe(
    String(Math.ceil(100 / DEFAULT_PAGE_SIZE))
  );

  const paginatorTotalEntriesInfo = container.querySelector("#total-entries");
  expect(paginatorTotalEntriesInfo.textContent).toBe("100");

  const selectedPaginatorButton = container.querySelector(
    ".Paginator .buttons .active"
  );
  expect(selectedPaginatorButton.textContent).toBe("1");
});

it("loads the appropriate page of data after clicking the paginator's buttons", async () => {
  act(() => {
    ReactDOM.render(<App />, container);
  });

  await act(async () => {
    jest.runAllTimers();
  });

  const buttonNext = container.querySelector(".Paginator .buttons :last-child");
  expect(buttonNext.textContent).toBe("Next");

  act(() => {
    buttonNext.dispatchEvent(new MouseEvent("click", { bubbles: true }));
  });

  await act(async () => {
    jest.runAllTimers();
  });

  let selectedPaginatorButton = container.querySelector(
    ".Paginator .buttons .active"
  );
  expect(selectedPaginatorButton.textContent).toBe("2");

  // clicking on button 9 loads the 9th page
  const button9 = container.querySelector(".Paginator .buttons :nth-child(10)");
  expect(button9.textContent).toBe("9");

  act(() => {
    button9.dispatchEvent(new MouseEvent("click", { bubbles: true }));
  });

  await act(async () => {
    jest.runAllTimers();
  });

  selectedPaginatorButton = container.querySelector(
    ".Paginator .buttons .active"
  );
  expect(selectedPaginatorButton.textContent).toBe("9");
});
