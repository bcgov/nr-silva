import React from "react";
import { 
    DatePicker, 
    DatePickerInput, 
    Dropdown,
    Table,
    TableHead,
    TableHeader,
    TableRow,
    TableBody,
    TableCell,
    ModalWrapper
 } from "@carbon/react";


import './Reports.scss';

const Reports: React.FC = () => {
    const items:String[] = ["Apple", "Mango", "Orange", "Peach"]

    const rows:any[] = [
            {
              id: 'load-balancer-1',
              name: 'Load Balancer 1',
              rule: 'Round robin',
              Status: 'Starting',
              other: 'Test',
              example: '22',
            },
            {
              id: 'load-balancer-2',
              name: 'Load Balancer 2',
              rule: 'DNS delegation',
              status: 'Active',
              other: 'Test',
              example: '22',
            },
            {
              id: 'load-balancer-3',
              name: 'Load Balancer 3',
              rule: 'Round robin',
              status: 'Disabled',
              other: 'Test',
              example: '22',
            },
            {
              id: 'load-balancer-4',
              name: 'Load Balancer 4',
              rule: 'Round robin',
              status: 'Disabled',
              other: 'Test',
              example: '22',
            },
            {
              id: 'load-balancer-5',
              name: 'Load Balancer 5',
              rule: 'Round robin',
              status: 'Disabled',
              other: 'Test',
              example: '22',
            },
            {
              id: 'load-balancer-6',
              name: 'Load Balancer 6',
              rule: 'Round robin',
              status: 'Disabled',
              other: 'Test',
              example: '22',
            },
            {
              id: 'load-balancer-7',
              name: 'Load Balancer 7',
              rule: 'Round robin',
              status: 'Disabled',
              other: 'Test',
              example: '22',
            },
          ];
    
    const headers:any[] = ['Name', 'Rule', 'Status', 'Other', 'Example'];
    return (
      <>
         <div className="contianer px-3">
          <div className="row py-4">
            <div className="col-md-12">
              <div className="display-5 text-start">Reports Page</div>
            </div>
          </div>
          <div className="h4">Form Sample</div>
          <div className="row">
            <div className="col-md-6">
                <DatePicker datePickerType="range">
                    <DatePickerInput
                        id="date-picker-input-id-start"
                        placeholder="mm/dd/yyyy"
                        labelText="Start date"
                        size="md"
                    />
                    <DatePickerInput
                        id="date-picker-input-id-finish"
                        placeholder="mm/dd/yyyy"
                        labelText="End date"
                        size="md"
                        
                    />
                </DatePicker>
                
            </div>

            <div className="col-md-6">
                  <Dropdown
                      id="default"
                      titleText="Select Fruit from Dropdown"
                      label="Dropdown menu options"
                      items={items}
                      itemToString={(item:String) => (item ? item : '')}
                  />
            </div>
          </div>

          <div className="row">
            <div className="col-md-4">
                  <Dropdown
                      id="default"
                      titleText="Select Fruit from Dropdown"
                      label="Dropdown menu options"
                      items={items}
                      itemToString={(item:String) => (item ? item : '')}
                  />
            </div>
            <div className="col-md-4">
                  <Dropdown
                      id="default"
                      titleText="Select Fruit from Dropdown"
                      label="Dropdown menu options"
                      items={items}
                      itemToString={(item:String) => (item ? item : '')}
                  />
            </div>
            <div className="col-md-4">
                  <Dropdown
                      id="default"
                      titleText="Select Fruit from Dropdown"
                      label="Dropdown menu options"
                      items={items}
                      itemToString={(item:String) => (item ? item : '')}
                  />
            </div>
          </div>

          <div className="row my-3">
            <ModalWrapper
              buttonTriggerText="Launch modal"
              modalHeading="Modal heading"
              modalLabel="Label"
              handleSubmit={() => {}}
              >
              <p>Modal content here</p>
            </ModalWrapper>
          </div>

          <div className="row pt-3">
            <div className="col-12">
            <Table size="lg" useZebraStyles={false}>
              <TableHead>
                <TableRow>
                  {headers.map((header) => (
                    <TableHeader id={header.key} key={header}>
                      {header}
                    </TableHeader>
                  ))}
                </TableRow>
              </TableHead>
              <TableBody>
                {rows.map((row) => (
                  <TableRow key={row.id}>
                    {Object.keys(row)
                      .filter((key) => key !== 'id')
                      .map((key) => {
                        return <TableCell key={key}>{row[key]}</TableCell>;
                      })}
                  </TableRow>
                ))}
              </TableBody>
            </Table>
            </div>
          </div>
         </div>
      </>
    );
  };

export default Reports;