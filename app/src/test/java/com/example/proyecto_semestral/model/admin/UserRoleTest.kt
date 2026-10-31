// Comprueba que solamente el dominio admin.cl reciba permisos administrativos.
package com.example.proyecto_semestral.model.admin

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UserRoleTest {

    @Test
    fun adminRoleRequiresExactAdminDomain() {
        assertTrue("pedro@admin.cl".isAdminEmail())
        assertTrue("  PEDRO@ADMIN.CL  ".isAdminEmail())
        assertEquals(UserRole.ADMIN, roleForEmail("persona@admin.cl"))
    }

    @Test
    fun similarOrMalformedDomainsAreNotAdmin() {
        assertFalse("pedro@otroadmin.cl".isAdminEmail())
        assertFalse("pedro@admin.cl.com".isAdminEmail())
        assertFalse("@admin.cl".isAdminEmail())
        assertFalse("pedro@@admin.cl".isAdminEmail())
        assertEquals(UserRole.USER, roleForEmail("persona@correo.cl"))
    }
}
