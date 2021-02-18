import React, { useRef } from 'react';
import { Form, FormGroup, FormControl, ControlLabel, HelpBlock, Schema, InputNumber, Checkbox, Row, Col, CheckboxGroup } from 'rsuite';

import simDefaults from '../data/simdefaults';

const { ArrayType, NumberType } = Schema.Types;
const model = Schema.Model({
  durationSeconds: NumberType().range(30, 600, "Valid fight durations are 30-600s").isRequired("A fight duration is required."),
  durationVariabilitySeconds: NumberType().range(0, 60, "Valid fight variability amounts are 0-60s").isRequired("A fight variability amount is required."),
  stepMs: NumberType().range(1, 1000, "Valid step durations are 1-1000ms").isRequired("A fight step size is required"),
  latencyMs: NumberType().range(0, 500, "Valid latencies are 0-500ms").isRequired("A latency amount is required"),
  iterations: NumberType().range(1, 10000, "Valid iteration counts are 1-10000").isRequired("Number of iterations is required"),
  targetLevel: NumberType().range(70, 73, "Valid target levels are 70-73").isRequired("Target level is required"),
  targetArmor: NumberType().range(0, 10000, "Valid target armor amounts are 0-10000").isRequired("Target armor is required"),
  allowParryAndBlock: ArrayType(),
  showHiddenBuffs: ArrayType()
});

const groupStyle = {
  marginBottom: '5px'
};

export default function({ dispatch }) {
  const form = useRef(null);

  function handleSubmit() {
    Object.entries(form.current.state.formValue).forEach(([key, val]) => {
      // InputNumber form fields set strings for some goddamned reason
      if(model.schema[key].name == 'number') {
        val = parseInt(val, 10)
      }

      // Checkboxes can only be tracked in forms via a CheckboxGroup
      // https://github.com/rsuite/rsuite/issues/818
      // So, the array is actually always just one number "1" as item zero, or empty
      if(model.schema[key].name == 'array') {
        val = val[0] === 1
      }

      // Dispatch state change
      dispatch({ type: key, value: val })
    });
  }

  function onCheck(formErr) {
    if(form && Object.keys(formErr).length === 0) {
      setTimeout(() => handleSubmit())
    }
  }

  return (
    <Form
      checkTrigger={'change'}
      formDefaultValue={simDefaults}
      model={model}
      onCheck={onCheck}
      ref={form}
    >
      <Row>
        <Col xs={12}>
          <FormGroup controlId="iterations" style={groupStyle}>
            <ControlLabel>Number of Iterations</ControlLabel>
            <FormControl name="iterations" accepter={InputNumber} />
            <HelpBlock tooltip>How many iterations to run, per simulation</HelpBlock>
          </FormGroup>
          <FormGroup controlId="durationSeconds" style={groupStyle}>
            <ControlLabel>Duration (seconds)</ControlLabel>
            <FormControl name="durationSeconds" accepter={InputNumber} />
            <HelpBlock tooltip>The fight length, in seconds</HelpBlock>
          </FormGroup>
          <FormGroup controlId="stepMs" style={groupStyle}>
            <ControlLabel>Iteration Step (ms)</ControlLabel>
            <FormControl name="stepMs" accepter={InputNumber} />
            <HelpBlock tooltip>How often to process the current simulation state.  Set this lower for greater accuracy, and higher for faster sims</HelpBlock>
          </FormGroup>
          <FormGroup controlId="latencyMs" style={groupStyle}>
            <ControlLabel>Latency (ms)</ControlLabel>
            <FormControl name="latencyMs" accepter={InputNumber} />
            <HelpBlock tooltip>Simulate this amount of latency, in milliseconds</HelpBlock>
          </FormGroup>
        </Col>
        <Col xs={12}>
          <FormGroup controlId="durationVariabilitySeconds" style={groupStyle}>
            <ControlLabel>Duration Variability (seconds)</ControlLabel>
            <FormControl name="durationVariabilitySeconds" accepter={InputNumber} />
            <HelpBlock tooltip>Randomly vary the length of each iteration by up to this amount, in seconds</HelpBlock>
          </FormGroup>
          <FormGroup controlId="targetLevel" style={groupStyle}>
            <ControlLabel>Target Level</ControlLabel>
            <FormControl name="targetLevel" accepter={InputNumber} />
            <HelpBlock tooltip>The level of the simulation target</HelpBlock>
          </FormGroup>
          <FormGroup controlId="targetArmor" style={groupStyle}>
            <ControlLabel>Target Armor</ControlLabel>
            <FormControl name="targetArmor" accepter={InputNumber} />
            <HelpBlock tooltip>The base armor value of the simulation target</HelpBlock>
          </FormGroup>
          <FormGroup controlId="allowParryAndBlock" style={groupStyle}>
            <ControlLabel>Allow Parry/Block?</ControlLabel>
            <FormControl name="allowParryAndBlock" accepter={CheckboxGroup}>
              <Checkbox value={1} />
            </FormControl>
            <HelpBlock tooltip>If checked, allows parry and block for melee simulation</HelpBlock>
          </FormGroup>
        </Col>
      </Row>
    </Form>
  )
}
