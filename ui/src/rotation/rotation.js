import React, { useState } from 'react';

import { Row, Col, Button } from 'rsuite';

export default function({ rotation, dispatch }) {
  const [jsonValid, setJsonValid] = useState(true);

  function saveRotation(value) {
    try {
      const parsed = JSON.parse(value);
      setJsonValid(true);
      dispatch({ type: 'character.rotation', value: parsed });
    } catch(e) {
      setJsonValid(false);
    }
  }

  const rotationStr = JSON.stringify(rotation, null, 2);

  return (
    <Row>
      <Col xs={24}>
        <textarea style={{
          backgroundColor: '#1a1d24',
          width: '100%',
          height: '100%',
          minHeight: '400px'
        }} value={rotationStr} />
        <Button enabled={jsonValid} onClick={saveRotation}>Save</Button>
      </Col>
    </Row>
  )
}
