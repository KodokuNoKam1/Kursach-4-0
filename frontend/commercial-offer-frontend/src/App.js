import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Login';
import Offers from './components/Offers';

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/offers" element={<Offers />} />
                <Route path="/" element={<Login />} />
            </Routes>
        </Router>
    );
};

export default App;