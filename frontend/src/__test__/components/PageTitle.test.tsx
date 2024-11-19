import React from 'react';
import { render, screen } from '@testing-library/react';
import PageTitle from '../../components/PageTitle';
import { describe, it, expect, vi } from 'vitest';
import { MemoryRouter, useLocation } from 'react-router-dom';

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import("react-router-dom")>("react-router-dom");
  return {
    ...actual,
    useLocation: vi.fn(),
  };
});

vi.mock('../../components/BCHeaderwSide/constants', async () => {
  const actual = await vi.importActual('../../components/BCHeaderwSide/constants');
  return {
    ...actual,
    leftMenu: [
      {
        name: 'Main',
        items: [
          {
            name: 'Submain',
            link: '/submain',
            breadcrumb: true,
            subItems: [
              {
                name: 'Test Page',
                link: '/test',
                breadcrumb: true,
              }
            ]
          },
          {
            name: 'Intramain',
            link: '/intramain',
            breadcrumb: false,
            subItems: [
              {
                name: 'Internal Affairs',
                link: '/intramain',
                breadcrumb: false,
              }
            ]
          }
        ]
      }
    ]
  };
});

describe('PageTitle Component', () => {

  it('renders the title correctly', () => {
    (useLocation as vi.Mock).mockReturnValue({ pathname: '/test' });
    render(
      <MemoryRouter initialEntries={['/test']}>
      <PageTitle 
        title="Test Title"
        subtitle='Not what you expected' />
      </MemoryRouter>
  );
    const titleElement = screen.getByText(/Test Title/i);
    expect(titleElement).toBeInTheDocument();
  });

  it('renders the breadcrumb correctly', () => {
    (useLocation as vi.Mock).mockReturnValue({ pathname: '/test' });
    render(
      <MemoryRouter initialEntries={['/test']}>
      <PageTitle 
        title="Test Title"
        subtitle='Not what you expected' />
      </MemoryRouter>
    );
    
    const titleElement = screen.getByText(/Submain/i);
    expect(titleElement).toBeInTheDocument();
  });

  it('renders empty breadcrumb if no path found on list', () => {
    (useLocation as vi.Mock).mockReturnValue({ pathname: '/homer' });
    render(
      <MemoryRouter initialEntries={['/homer']}>
      <PageTitle 
        title="Test Title"
        subtitle='Not what you expected' />
      </MemoryRouter>
    );
    
    const olElement = screen.getByRole('list');
    const listItems = olElement.querySelectorAll('li');    
    expect(olElement).toBeInTheDocument();
    expect(listItems.length).toBe(0);
  });

  it('should not render breadcrumb if breadcrumb is false', () => {
    (useLocation as vi.Mock).mockReturnValue({ pathname: '/intramain' });
    render(
      <MemoryRouter initialEntries={['/intramain']}>
      <PageTitle 
        title="Internal Affairs"
        subtitle='Your secret, our secret' />
      </MemoryRouter>
    );

    const olElement = screen.getByRole('list');
    const listItems = olElement.querySelectorAll('li');    
    expect(olElement).toBeInTheDocument();
    expect(listItems.length).toBe(0);
  });

});