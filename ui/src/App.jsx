import React, { useReducer } from 'react';
import {
  BrowserRouter as Router,
  Route,
  Routes,
  useNavigate
} from 'react-router-dom';
import _ from 'lodash';
import { FaGithub } from "react-icons/fa";
import { Container, Content, CustomProvider, Header, Grid, Footer, Row, Col, Button, Panel, Navbar, Nav, Message } from 'rsuite';
import { Icon } from '@rsuite/icons';

import { StateProvider, useDispatchContext, useStateContext } from './state';

import EquivalencePoints from './ep/equivalence_points';
import GearEP from './gear/gear_ep';
import Rankings from './rankings/rankings';
import Simulator from './sim/simulator';

import * as tbcsim from 'tbcsim';

import './App.css';

function App() {
  const state = useStateContext();
  const dispatch = useDispatchContext();
  const navigate = useNavigate();

  return (
    <Container style={{ height: '100%' }}>
      <Header>
        <Navbar>
          <Navbar.Content>
            <Navbar.Brand>
              <h4 style={{ lineHeight: '0px', cursor: 'pointer' }} onClick={() => { navigate("/")} }>TBCSim</h4>
            </Navbar.Brand>
            <Nav>
              <Nav.Item onSelect={() => { navigate("/")} }>Simulator</Nav.Item>
              <Nav.Item onSelect={() => { navigate("/gear")} }>Gear EP</Nav.Item>
              <Nav.Item onSelect={() => { navigate("/ep")} }>Equivalence Points</Nav.Item>
              <Nav.Item onSelect={() => { navigate("/rankings")} }>Rankings</Nav.Item>
            </Nav>
            <Nav>
              <Nav.Item
                icon={<Icon as={FaGithub} />}
                style={{ color: '#e9ebf0', textDecoration: 'none' }}
                href='https://github.com/secretbis/tbcsim/issues/new'
                target='_blank'
                rel='noreferrer noopener'>Report a Bug
              </Nav.Item>
            </Nav>
          </Navbar.Content>
        </Navbar>
      </Header>
      <Routes>
        <Route path="/" element={ <Simulator /> } />
        <Route path="/gear" element={ <GearEP /> } />
        <Route path="/ep" element={ <EquivalencePoints /> } />
        <Route path="/rankings" element={ <Rankings /> } />
      </Routes>
      <Footer>
        <Navbar className='justify-content-center'>
          <Navbar.Content>
            <Nav>
              <Nav.Item></Nav.Item>
            </Nav>
          </Navbar.Content>
        </Navbar>
      </Footer>
    </Container>
  );
}

export default function() {
  return (
    <Router>
      <CustomProvider theme="dark">
        <StateProvider>
          <App />
        </StateProvider>
      </CustomProvider>
    </Router>
  )
};
