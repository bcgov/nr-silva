import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";
import ChartContainer from "../../components/ChartContainer";

const title = "Test Title";
const description = "Test Description";

describe("ChartContainer Component", () => {
  it("should render the chart container with title and description", () => {
    render(<ChartContainer title={title} description={description} />);
    expect(screen.getByText(title)).toBeDefined();
    expect(screen.getByText(description)).toBeDefined();
  });

  it("should render children components", () => {
    render(
      <ChartContainer title={title} description={description}>
        <div>Test Child</div>
      </ChartContainer>
    );
    expect(screen.getByText("Test Child")).toBeDefined();
  });
});
