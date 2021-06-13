import React, { useState } from 'react';
import _ from 'lodash';
import { CheckPicker, Button, Container, InputNumber, Modal } from 'rsuite';

import { kprop } from '../util/util';
import * as tbcsim from 'tbcsim';

const eventTemplates = {
  BUFF_START: evt => {
    return `You gain ${kprop(evt.buff, 'name')}`;
  },
  BUFF_REFRESH: evt => {
    const stacks = evt.buffStacks ? ` (${evt.buffStacks} stacks)` : '';
    const charges = evt.buffCharges ? ` (${evt.buffCharges} charges)` : '';
    return `${kprop(evt.buff, 'name')} on You is refreshed${stacks}${charges}`;
  },
  BUFF_CHARGE_CONSUMED: evt => {
    return `Consumed ${kprop(evt.buff, 'name')} charge (${evt.buffCharges} left)`
  },
  BUFF_END: evt => {
    return `${kprop(evt.buff, 'name')} on You ends`
  },
  DEBUFF_START: evt => {
    return `Target gains ${kprop(evt.buff, 'name')}`
  },
  DEBUFF_REFRESH: evt => {
    return `${kprop(evt.buff, 'name')} on Target is refreshed ${evt.buffStacks}`
  },
  DEBUFF_CHARGE_CONSUMED: evt => {
    return `Target ${kprop(evt.buff, 'name')} charge consumed: ${kprop(evt.buff, 'buffCharges')}`
  },
  DEBUFF_END: evt => {
    return `${kprop(evt.buff, 'name')} on Target ends`
  },
  DAMAGE: evt => {
    return `Your ${evt.abilityName} deals ${evt.amount.toFixed(0)} damage (${_.capitalize(kprop(evt.result, 'name', ''))})`
  },
  RESOURCE_CHANGED: evt => {
    const type = evt.delta >= 0 ? 'gain' : 'lose';
    return `You ${type} ${Math.abs(evt.delta).toFixed(0)} ${kprop(evt.resourceType, 'name').toLowerCase()} (${evt.abilityName}) (current: ${evt.amount.toFixed(0)})`
  },
  SPELL_START_CAST: evt => {
    return `You begin casting ${evt.abilityName}`
  },
  SPELL_CAST: evt => {
    return `You finish casting ${evt.abilityName}`
  },
}

const filterData = Object.keys(eventTemplates).map(key => {
  return {
    label: key,
    value: key
  }
})

const defaultSelected = _.without(Object.keys(eventTemplates), ...['SPELL_CAST'])
const allFilters = Object.keys(eventTemplates).map(key => {
  return {
    label: key,
    value: key
  }
});

export default function({ iterations }) {
  if(!iterations) return null

  const [modalOpen, setModalOpen] = useState(false);
  const [selectedIteration, setSelectedIteration] = useState(0);
  const [selectedFilters, setSelectedFilters] = useState(defaultSelected);

  const iterationHasPet = false;
  const participantOptions = iterationHasPet ? ['subject', 'pet'] : ['subject']
  const [selectedParticipant, setSelectedParticipant] = useState('subject');

  function onSelectedIterationChange(value) {
    if(kprop(iterations, 'array')[value] != null) {
      setSelectedIteration(value)
    }
  }

  function onCombatLogClick() {
    setModalOpen(true)
  }

  function onHide() {
    setModalOpen(false);
  }

  function logTimestamp(millisElapsed) {
    const second = 1000
    const minute = second * 60

    const minutes = `${Math.floor(millisElapsed / minute)}`.padStart(2, '0')
    const seconds = `${Math.floor(millisElapsed % minute / second)}`.padStart(2, '0')
    const millis = `${Math.floor(millisElapsed % minute % second)}`.padStart(3, '0')

    return `[${minutes}:${seconds}:${millis}]`
  }

  function shouldRenderEvent(event) {
    const isHiddenBuff = kprop(event.buff, 'hidden');
    const isFiltered = !selectedFilters.includes(kprop(event.eventType, 'name'))
    return !(isHiddenBuff || isFiltered)
  }

  function renderLog() {
    const events = kprop(kprop(iterations, 'array')[selectedIteration][selectedParticipant].events, 'array')
    return (
      <Container>
        {events.map((event, idx) => {
          if(!shouldRenderEvent(event)) return null;

          const template = eventTemplates[event.eventType]
          if(template) {
            return (
              <div key={idx}>
                <span>{logTimestamp(event.timeMs)}</span>
                &nbsp;
                <span>{template(event)}</span>
              </div>
            );
          } else {
            return (
              <div key={idx}>
                <span>{logTimestamp(event.timeMs)}</span>
                &nbsp;
                <span>{kprop(event.eventType, 'name')}</span>
              </div>
            )
          }
        })}
      </Container>
    )
  }

  function iterationDps() {
    const dps = tbcsim.sim.SimStats.dps_0(
      tbcsim.util.Utils.listWrap([kprop(iterations, 'array')[selectedIteration]])
    )

    // Median of 1, omegalul
    const subjectDpsMedian = dps.get_35('subject').median
    const subjectPetDpsMedian = (dps.get_35('subjectPet') || {}).median || 0
    const totalMedian = subjectDpsMedian + subjectPetDpsMedian

    const totalDps = {
      total: subjectDpsMedian + subjectPetDpsMedian,
      petPct: subjectPetDpsMedian / totalMedian * 100.0,
      youPct: subjectDpsMedian / totalMedian * 100.0
    }

    return (
      <strong>{totalDps.total.toFixed(2)}</strong>
    )
  }

  function onFilterChange(evt) {
    setSelectedFilters(evt);
  }

  return (
    <span style={{ marginLeft: 10 }}>
      <Button appearance='ghost' onClick={onCombatLogClick}>Combat Log</Button>
      <Modal show={modalOpen} full size='lg' onHide={onHide} style={{ maxHeight: '80vh' }}>
        <Modal.Header style={{ marginBottom: 10 }}>
          <Modal.Title style={{ marginBottom: 10 }}>Combat Log</Modal.Title>
          <div style={{ marginBottom: 10 }}><InputNumber prefix='Iteration: ' style={{ width: 170 }} min={0} max={iterations.size - 1} value={selectedIteration} onChange={onSelectedIterationChange} /></div>
          <div style={{ marginBottom: 10 }}>Iteration DPS: {iterationDps()}</div>
          <div style={{ marginBottom: 10 }}>Filter Events: <CheckPicker value={selectedFilters} data={allFilters} defaultValue={defaultSelected} onChange={onFilterChange} /></div>
        </Modal.Header>
        <Modal.Body>
          <div>
            {renderLog()}
          </div>
        </Modal.Body>
      </Modal>
    </span>
  )
}
