import React, { useState } from 'react';
import { Form, InputNumber, Checkbox, Row, Col, CheckboxGroup } from 'rsuite';

import { useDispatchContext, useStateContext } from '../state';

import { SchemaModel, BooleanType, NumberType } from 'schema-typed';
const model = SchemaModel({
  durationSeconds: NumberType().range(30, 600, "Valid fight durations are 30-600s").isRequired("A fight duration is required."),
  durationVariabilitySeconds: NumberType().range(0, 60, "Valid fight variability amounts are 0-60s").isRequired("A fight variability amount is required."),
  stepMs: NumberType().range(1, 1000, "Valid step durations are 1-1000ms").isRequired("A fight step size is required"),
  latencyMs: NumberType().range(0, 500, "Valid latencies are 0-500ms").isRequired("A latency amount is required"),
  iterations: NumberType().range(1, 10000, "Valid iteration counts are 1-10000").isRequired("Number of iterations is required"),
  targetLevel: NumberType().range(70, 73, "Valid target levels are 70-73").isRequired("Target level is required"),
  targetType: NumberType().range(0, 9, "Valid target types are 0-9, corresponding to internal constants"),
  targetArmor: NumberType().range(0, 10000, "Valid target armor amounts are 0-10000").isRequired("Target armor is required"),
  allowParryAndBlock: BooleanType(),
  showHiddenBuffs: BooleanType()
});

const groupStyle = {
  marginBottom: '5px'
};

export default function() {
  const { simOptions } = useStateContext();
  const dispatch = useDispatchContext();

  function onChange(formValue) {
    Object.entries(formValue).forEach(([key, val]) => {
      // InputNumber form fields set strings for some goddamned reason
      if(model.$spec[key].$typeName == 'number') {
        val = parseInt(val, 10)
      }

      // Dispatch state change
      dispatch({ type: `simOptions.${key}`, value: val })
    });
  }

  return (
    <Form
      checkTrigger={'change'}
      formValue={simOptions}
      model={model}
      onChange={onChange}
    >
      <Row>
        <Col xs={12}>
          <Form.Stack layout={'horizontal'}>
            <Form.Group controlId="iterations" style={groupStyle}>
              <Form.ControlLabel>Number of Iterations</Form.ControlLabel>
              <Form.Control name="iterations" accepter={InputNumber} />
              <Form.Text tooltip>How many iterations to run, per simulation</Form.Text>
            </Form.Group>
            <Form.Group controlId="durationSeconds" style={groupStyle}>
              <Form.ControlLabel>Duration (seconds)</Form.ControlLabel>
              <Form.Control name="durationSeconds" accepter={InputNumber} />
              <Form.Text tooltip>The fight length, in seconds</Form.Text>
            </Form.Group>
            <Form.Group controlId="stepMs" style={groupStyle}>
              <Form.ControlLabel>Iteration Step (ms)</Form.ControlLabel>
              <Form.Control name="stepMs" accepter={InputNumber} />
              <Form.Text tooltip>How often to process the current simulation state.  Set this lower for greater accuracy, and higher for faster sims</Form.Text>
            </Form.Group>
            <Form.Group controlId="latencyMs" style={groupStyle}>
              <Form.ControlLabel>Latency (ms)</Form.ControlLabel>
              <Form.Control name="latencyMs" accepter={InputNumber} />
              <Form.Text tooltip>Simulate this amount of latency, in milliseconds</Form.Text>
            </Form.Group>
          </Form.Stack>
        </Col>
        <Col xs={12}>
          <Form.Stack layout={'horizontal'}>
            <Form.Group controlId="durationVariabilitySeconds" style={groupStyle}>
              <Form.ControlLabel>Duration Variability (seconds)</Form.ControlLabel>
              <Form.Control name="durationVariabilitySeconds" accepter={InputNumber} />
              <Form.Text tooltip>Randomly vary the length of each iteration by up to this amount, in seconds</Form.Text>
            </Form.Group>
            <Form.Group controlId="targetLevel" style={groupStyle}>
              <Form.ControlLabel>Target Level</Form.ControlLabel>
              <Form.Control name="targetLevel" accepter={InputNumber} />
              <Form.Text tooltip>The level of the simulation target</Form.Text>
            </Form.Group>
            <Form.Group controlId="targetArmor" style={groupStyle}>
              <Form.ControlLabel>Target Armor</Form.ControlLabel>
              <Form.Control name="targetArmor" accepter={InputNumber} />
              <Form.Text tooltip>The base armor value of the simulation target</Form.Text>
            </Form.Group>
            <Form.Group controlId="allowParryAndBlock" style={groupStyle}>
              <Form.ControlLabel>Allow Parry/Block?</Form.ControlLabel>
              <Form.Control name="allowParryAndBlock" accepter={Checkbox} defaultChecked />
              <Form.Text tooltip>If checked, allows parry and block for melee simulation</Form.Text>
            </Form.Group>
            <Form.Group controlId="showHiddenBuffs" style={groupStyle}>
              <Form.ControlLabel>Show hidden buffs?</Form.ControlLabel>
              <Form.Control name="showHiddenBuffs" accepter={Checkbox} defaultChecked />
              <Form.Text tooltip>This is a debugging option to show extremely verbose buffs, that are normally invisible to the player.</Form.Text>
            </Form.Group>
          </Form.Stack>
        </Col>
      </Row>
    </Form>
  )
}
