import React from "react";
import ReactDOM from "react-dom";
import { act } from "react-dom/test-utils";
import App from "../App";
import {DEFAULT_PAGE_SIZE} from "../../AppContext";


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

it("should show buttons appropriately", async () => {
  act(() => {
    ReactDOM.render(<App />, container);
  });

  await act(async () => {
    jest.runAllTimers();
  });

  // 1 button for the 10 pages and the 'Next' button
  let paginatorButtons = document.querySelectorAll(
    ".Paginator .buttons button"
  );
  expect(paginatorButtons.length).toBe(Math.ceil(100 / DEFAULT_PAGE_SIZE) + 1);

  // buton '1' must be active
  let selectedPaginatorButton = container.querySelector(
    ".Paginator .buttons .active"
  );
  expect(selectedPaginatorButton.textContent).toBe("1");

  // last button must be the 'Next' button
  let lastButton = container.querySelector(".Paginator .buttons :last-child");
  expect(lastButton.textContent).toBe("Next");

  act(() => {
    lastButton.dispatchEvent(new MouseEvent("click", { bubbles: true }));
  });

  await act(async () => {
    jest.runAllTimers();
  });

  // when the 'Next' button is clicked for the very fist time, buttons for each page plus 'Next' and 'Previous'
  paginatorButtons = document.querySelectorAll(".Paginator .buttons button");
  expect(paginatorButtons.length).toBe( Math.ceil(100 / DEFAULT_PAGE_SIZE) + 2); // 'Previous' button, 10 buttons for each page, 'Next' button

  // ... and the button '2' is the active one
  selectedPaginatorButton = container.querySelector(
    ".Paginator .buttons .active"
  );
  expect(selectedPaginatorButton.textContent).toBe("2");

  // given the last numerical button , when clicked, the 'next' button must hide, once there is not next pages
  let lastButOneButton = document.querySelector(
    ".Paginator .buttons :nth-last-child(2)"
  );

  act(() => {
    lastButOneButton.dispatchEvent(new MouseEvent("click", { bubbles: true }));
  });
  await act(async () => {
    jest.runAllTimers();
  });
  


  lastButton = container.querySelector(".Paginator .buttons :last-child");
  expect(lastButton.textContent).not.toBe("Next");
});

it("FileService should request new pages to the backend when buttons are clicked", async () => {
  act(() => {
    ReactDOM.render(<App />, container);
  });

  await act(async () => {
    jest.runAllTimers();
  });

  let nextButton = container.querySelector(".Paginator .buttons :last-child");
  expect(nextButton.textContent).toBe("Next");

  act(() => {
    nextButton.dispatchEvent(new MouseEvent("click", { bubbles: true }));
  });

  await act(async () => {
    jest.runAllTimers();
  });

  act(() => {
    nextButton.dispatchEvent(new MouseEvent("click", { bubbles: true }));
  });

  await act(async () => {
    jest.runAllTimers();
  });

  // the 2 click on the button next + on first render of the component
  expect(mockFileServiceList.mock.calls.length).toBe(3);
  expect(mockFileServiceList.mock.calls[0]).toEqual([0, DEFAULT_PAGE_SIZE,undefined]);
  expect(mockFileServiceList.mock.calls[1]).toEqual([1, DEFAULT_PAGE_SIZE,undefined]);
  expect(mockFileServiceList.mock.calls[2]).toEqual([2, DEFAULT_PAGE_SIZE,undefined]);
});
