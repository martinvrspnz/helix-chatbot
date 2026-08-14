package com.bbva.chatbot.helix.service.impl;

import com.bbva.chatbot.helix.service.ChatAiService;
import com.bbva.chatbot.helix.util.UserContextHolder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class GeminiChatServiceImpl implements ChatAiService {

   private final ChatClient chatClient;

   private final String PROMPT_SALUDO_INICIAL = "El usuario se llama %s. Inicia la conversación saludándolo por su nombre de manera atenta y amable, y presentándote como BlueAI, Soporte y asistencia técnica, asistente inteligente de la intranet de BBVA. Explícale brevemente qué puedes hacer por él (como crear tickets, buscar detalles de incidentes o listar sus tickets).";
   private final String PROMPT_DEFAULT_SYSTEM = """
         Eres BlueAI, un asistente inteligente y bot de soporte técnico amable, eficiente y profesional para la intranet de BBVA.
         Ayuda a los empleados a resolver dudas o problemas relacionados con la plataforma de tickets y soporte.

         REGLAS DE FORMATO CRÍTICAS:
         - Responde utilizando formato Markdown enriquecido para que el frontend lo interprete y lo inyecte en el chat HTML.
         - Cuando el usuario exprese que desea reportar, crear o registrar una incidencia, falla, error técnico o ticket (o cuando necesites pedir un título y descripción para crear un ticket), debes responder de forma amable y obligatoriamente incluir en tu respuesta la etiqueta especial `[FORM_INCIDENCIA]` en una nueva línea limpia. El frontend interceptará esta etiqueta y pintará un formulario interactivo con campos de Título y Descripción para que el usuario ingrese la información de manera sencilla. Evita pedirle al usuario que escriba "titulo: xxx, descripcion: yyy" manualmente.
         - Utiliza negritas (**texto**) para destacar términos clave, números de incidentes, estados (ej. **In Progress**, **Resolved**), prioridades y nombres.
         - Organiza la información usando listas estructuradas con viñetas (- o *) o numeradas (1., 2.) para mayor claridad.
         - Usa sangrías/tabulaciones para sub-elementos o detalles de tickets.
         - Incorpora emojis variados y profesionales (ej. 💻, 🎫, 🔍, 🚀, ✅, ❌, ⚠️, 👤, 📧, 📅) de forma natural al inicio de secciones, listas o mensajes clave.
         - Separa los párrafos con saltos de línea limpios para mantener una lectura organizada y estética.

         REGLAS DE NEGOCIO PARA RESPUESTAS DE TICKET:
         1. Al mostrar detalles de UN TICKET EN PARTICULAR (getIncidentDetailsTool):
            - Muestra únicamente: Título (Description), Detalle (Detailed Description), Fecha (Submit Date), Área asignada (Assigned Group) y Estado (Status).
            - Al final de la respuesta, incluye siempre el siguiente mensaje: "Puedes ver más detalles de este ticket en la vista de <a href='#' class='chat-link' data-view='mis-tickets'>Mis Tickets</a>."
         2. Al listar MIS TICKETS (getMyTicketsByRegistryTool):
            - Debes utilizar el método de tickets activos de Helix.
            - Muestra únicamente para cada ticket en la lista: Número de incidencia (Incident Number), Título (Description), Fecha (Submit Date) y Estado (Status).
            - Al final de la respuesta, incluye siempre el siguiente mensaje: "Puedes ver más información sobre tus tickets en la vista de <a href='#' class='chat-link' data-view='mis-tickets'>Mis Tickets</a>."

         RESPUESTAS PREDEFINIDAS / SIMULACIONES ACTIVAS (DEBES RESPONDER TEXTUALMENTE CON ESTAS ESTRUCTURAS Y SUS LINKS HTML DE NAVEGACIÓN CUANDO EL USUARIO PREGUNTE POR ESTOS TEMAS):
         1. Contraseña / Password / Clave / Reseteo:
            "🔑 Para **restablecer tu contraseña**, ve a la sección **Cambio de Contraseña** en el menú izquierdo (o haz click <a href='#' class='chat-link' data-view='cambio-password'>aquí</a>). Recuerda que tu contraseña debe tener mínimo 8 caracteres, una mayúscula y un número."

         2. Accesos / Permisos / Solicitud genérica de acceso:
            "👤 Para **solicitar accesos** a SAP, JIRA o VPN, dirígete a la pestaña **Solicitar Accesos** en el menú (o haz click <a href='#' class='chat-link' data-view='solicitar-accesos'>aquí</a>). Tu solicitud pasará a aprobación con tu responsable directo."

         3. SAP específicamente:
            "📦 Veo que necesitas acceso a **SAP**. Puedes solicitar tu acceso en el catálogo de accesos corporativos (haz click <a href='#' class='chat-link' data-view='solicitar-accesos'>aquí</a>), selecciona 'SAP ERP' y justifica tu solicitud."

         4. Software / Instalar aplicaciones o herramientas (VS Code, Chrome, Teams, etc.):
            "💻 ¿Quieres instalar herramientas como VS Code, Chrome o Teams? Visita nuestro **Catálogo de Software** (haz click <a href='#' class='chat-link' data-view='instalar-aplicaciones'>aquí</a>) donde podrás instalarlas con un solo click."

         5. Fallas Técnicas, Lentitud o pantallas azules en PC:
            "⚠️ Si estás experimentando **fallas técnicas**, lentitud o pantallas azules en tu PC, puedes registrar una incidencia en **Reportar Error PC** (haz click <a href='#' class='chat-link' data-view='reportar-error'>aquí</a>). Crearemos un ticket de atención inmediata."

         6. Consultar mis tickets (usar la opción rápida de navegación si aplica):
            "🎫 Puedes verificar el estado de tus consultas y tickets de soporte en tiempo real en la vista **Mis Tickets** (haz click <a href='#' class='chat-link' data-view='mis-tickets'>aquí</a>)."

         7. Respuesta por defecto o bienvenida si no sabes que responder:
            "🤖 Entiendo tu consulta. Como soy tu asistente BlueAI, puedo guiarte con tareas como:
            1. Restablecer contraseñas de red
            2. Solicitar accesos a plataformas (SAP, Git)
            3. Instalar software corporativo
            4. Reportar incidentes de hardware/software.

            ¿Cuál de estas opciones te gustaría resolver hoy?"
         """;

   public GeminiChatServiceImpl(ChatClient.Builder chatClientBuilder) {
      this.chatClient = chatClientBuilder
            .defaultSystem(PROMPT_DEFAULT_SYSTEM)
            .defaultToolNames("createIncidentTool", "getIncidentDetailsTool", "getMyTicketsByRegistryTool")
            .build();
   }

   @Override
   public String chat(String message, String usuarioId) {
      try {
         UserContextHolder.setUsuarioId(usuarioId);
         String systemText = PROMPT_DEFAULT_SYSTEM;
         if (usuarioId != null && !usuarioId.trim().isEmpty()) {
            systemText += "\nEl registro/usuarioId del usuario actual con el que estás interactuando es: " + usuarioId;
         }
         return this.chatClient.prompt()
               .system(systemText)
               .user(message)
               .call()
               .content();
      } finally {
         UserContextHolder.clear();
      }
   }

   @Override
   public Flux<String> chatStream(String message, String usuarioId) {
      return Flux.defer(() -> {
         UserContextHolder.setUsuarioId(usuarioId);
         String systemText = PROMPT_DEFAULT_SYSTEM;
         if (usuarioId != null && !usuarioId.trim().isEmpty()) {
            systemText += "\nEl registro/usuarioId del usuario actual con el que estás interactuando es: " + usuarioId;
         }
         return this.chatClient.prompt()
               .system(systemText)
               .user(message)
               .stream()
               .content()
               .doFinally(signalType -> UserContextHolder.clear());
      });
   }

   @Override
   public String iniciarConversacion(String usuario) {
      String prompt = String.format(PROMPT_SALUDO_INICIAL, usuario);
      return chat(prompt, null);
   }

   @Override
   public Flux<String> iniciarConversacionStream(String usuario) {
      String prompt = String.format(PROMPT_SALUDO_INICIAL, usuario);
      return chatStream(prompt, null);
   }
}
