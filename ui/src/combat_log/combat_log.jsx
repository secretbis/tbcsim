import React, { useState } from 'react';
import _ from 'lodash';
import { CheckPicker, Button, Container, InputNumber, Modal } from 'rsuite';

import * as tbcsim from 'tbcsim';

const eventTemplates = {
  BUFF_START: evt => {
    return `You gain ${evt.buff.name}`;
  },
  BUFF_REFRESH: evt => {
    const stacks = evt.buffStacks ? ` (${evt.buffStacks} stacks)` : '';
    const charges = evt.buffCharges ? ` (${evt.buffCharges} charges)` : '';
    return `${evt.buff.name} on You is refreshed${stacks}${charges}`;
  },
  BUFF_CHARGE_CONSUMED: evt => {
    return `Consumed ${evt.buff.name} charge (${evt.buffCharges} left)`
  },
  BUFF_END: evt => {
    return `${evt.buff.name} on You ends`
  },
  DEBUFF_START: evt => {
    return `Target gains ${evt.buff.name}`
  },
  DEBUFF_REFRESH: evt => {
    return `${evt.buff.name} on Target is refreshed ${evt.buffStacks}`
  },
  DEBUFF_CHARGE_CONSUMED: evt => {
    return `Target ${evt.buff.name} charge consumed: ${evt.buff.buffCharges}`
  },
  DEBUFF_END: evt => {
    return `${evt.buff.name} on Target ends`
  },
  DAMAGE: evt => {
    return `Your ${evt.ability && evt.ability.name} deals ${evt.amount.toFixed(0)} damage (${_.capitalize(evt.result.name)})`
  },
  RESOURCE_CHANGED: evt => {
    const type = evt.delta >= 0 ? 'gain' : 'lose';
    return `You ${type} ${Math.abs(evt.delta).toFixed(0)} ${evt.resourceType.name.toLowerCase()} (${evt.ability && evt.ability.name}) (current: ${evt.amount.toFixed(0)})`
  },
  SPELL_START_CAST: evt => {
    return `You begin casting ${evt.ability && evt.ability.name}`
  },
  SPELL_CAST: evt => {
    return `You finish casting ${evt.ability && evt.ability.name}`
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
    if(iterations.asJsReadonlyArrayView()[value] != null) {
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
    const isHiddenBuff = event.buff && event.buff.hidden;
    const isFiltered = !selectedFilters.includes(event.eventType.name)
    return !(isHiddenBuff || isFiltered)
  }

  function renderLog() {
    const events = iterations.asJsReadonlyArrayView()[selectedIteration][selectedParticipant].events.asJsReadonlyArrayView()
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
                <span>{event.eventType.name}</span>
              </div>
            )
          }
        })}
      </Container>
    )
  }

  function iterationDps() {
    const dps = tbcsim.SimStats.getInstance().dps(
      tbcsim.Utils.getInstance().listWrap([iterations.asJsReadonlyArrayView()[selectedIteration]])
    )

    const subjectDpsMedian = dps.asJsMapView().get('subject').median
    const subjectPetDpsMedian = (dps.asJsMapView().get('subjectPet') || {}).median || 0
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
      <Modal open={modalOpen} full size='lg' onHide={onHide} onClose={onHide} style={{ maxHeight: '80vh' }}>
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
