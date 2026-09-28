import React from "react";
import { render, screen, act } from "@testing-library/react";
import { describe, expect, it } from "vitest";
import { ModalProvider, useModal } from "../../contexts/ModalContext";

const TestComponent = () => {
  const { openModal, closeModal, modalKey, isOpen } = useModal();

  return (
    <div>
      <span data-testid="is-open">{isOpen ? "true" : "false"}</span>
      <span data-testid="modal-key">{modalKey ?? "none"}</span>
      <button onClick={() => openModal("CREATE_OPENING")}>Open Create Opening</button>
      <button onClick={closeModal}>Close Modal</button>
    </div>
  );
};

describe("ModalContext", () => {
  it("provides initial closed state", () => {
    render(
      <ModalProvider>
        <TestComponent />
      </ModalProvider>
    );

    expect(screen.getByTestId("is-open").textContent).toBe("false");
    expect(screen.getByTestId("modal-key").textContent).toBe("none");
  });

  it("opens modal when openModal is called and closes on closeModal", () => {
    render(
      <ModalProvider>
        <TestComponent />
      </ModalProvider>
    );

    act(() => {
      screen.getByText("Open Create Opening").click();
    });

    expect(screen.getByTestId("is-open").textContent).toBe("true");
    expect(screen.getByTestId("modal-key").textContent).toBe("CREATE_OPENING");

    act(() => {
      screen.getByText("Close Modal").click();
    });

    expect(screen.getByTestId("is-open").textContent).toBe("false");
    expect(screen.getByTestId("modal-key").textContent).toBe("none");
  });

  it("throws error when useModal is called outside ModalProvider", () => {
    const ComponentWithoutProvider = () => {
      useModal();
      return <div>No Provider</div>;
    };

    expect(() => render(<ComponentWithoutProvider />)).toThrow(
      "useModal must be used within a ModalProvider"
    );
  });
});
