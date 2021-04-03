import React, { useEffect, useState } from 'react';

import { Row, Col, Button } from 'rsuite';

function stringifyRotation(rotation) {
  return rotation ? JSON.stringify(rotation, null, 2) : '';
}

export default function({ rotation, dispatch }) {
  const [jsonValid, setJsonValid] = useState(true);
  const [rotationStr, setRotationStr] = useState(rotation);

  useEffect(() => {
    setRotationStr(stringifyRotation(rotation));
  }, [rotation])

  function saveRotation(evt) {
    try {
      const parsed = JSON.parse(rotationStr);
      setJsonValid(true);
      dispatch({ type: 'character.rotation', value: parsed });
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

    setRotationStr(value)
  }

  return (
    <Row>
      <Col xs={24}>
        <textarea style={{
          backgroundColor: '#1a1d24',
          width: '100%',
          height: '100%',
          minHeight: '400px'
        }} value={rotationStr} onChange={onChange} />
        <Button disabled={!rotationStr || !jsonValid} onClick={saveRotation}>Save</Button>
      </Col>
    </Row>
  )
}
