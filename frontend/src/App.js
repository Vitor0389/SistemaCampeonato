import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import MainPage from './pages/MainPage';
import CriarCampeonatoPage from './pages/CriarCampeonatoPage';
import BuscarCampeonatosPage from './pages/BuscarCampeonatosPage';
import DetalhesCampeonatoPage from './pages/DetalhesCampeonatoPage';
import DeletarCampeonatoPage from './pages/DeletarCampeonatoPage';
import RegistrarResultadoPage from './pages/RegistrarResultadoPage';

function App() {
    const isAuthenticated = !!localStorage.getItem('token');

    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to={isAuthenticated ? '/main' : '/login'} replace />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />

                {/* Rotas protegidas após login */}
                <Route path="/main" element={<MainPage />} />
                <Route path="/criar-campeonato" element={<CriarCampeonatoPage />} />
                <Route path="/campeonatos" element={<BuscarCampeonatosPage />} />
                <Route path="/detalhes-campeonato/:id" element={<DetalhesCampeonatoPage />} />
                <Route path="/deletar" element={<DeletarCampeonatoPage />} />
                <Route path="/registrar-resultado" element={<RegistrarResultadoPage />} />
            </Routes>
        </Router>
    );
}

export default App;
