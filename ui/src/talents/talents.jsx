import React, { useEffect, useState } from 'react';
import { Row, Col, Button } from 'rsuite';

import { useDispatchContext, useStateContext } from '../state';

function stringifyTalents(talents) {
  return talents ? JSON.stringify(talents, null, 2) : '';
}

export default function() {
  const { talents } = useStateContext();
  const dispatch = useDispatchContext();

  const [jsonValid, setJsonValid] = useState(true);
  const [talentsStr, setTalentsStr] = useState(talents);

  useEffect(() => {
    setTalentsStr(stringifyTalents(talents));
  }, [talents])

  function saveTalents(evt) {
    try {
      const parsed = JSON.parse(talentsStr);
      setJsonValid(true);
      dispatch({ type: 'setTalents', value: parsed });
    } catch(e) {
      setJsonValid(false);
    }
  }

  function onChange(e) {
    const value = e.target.value;
    try {
      const parsed = JSON.parse(value);
      setJsonValid(true);
    } catch(e) {
      setJsonValid(false);
    }

    setTalentsStr(value)
  }

  return (
    <Row>
      <Col xs={24}>
        <textarea style={{
          backgroundColor: '#1a1d24',
          width: '100%',
          height: '100%',
          minHeight: '400px'
        }} value={talentsStr || ""} onChange={onChange} />
        <Button disabled={!talentsStr || !jsonValid} onClick={saveTalents}>Save</Button>
      </Col>
    </Row>
  )
}
