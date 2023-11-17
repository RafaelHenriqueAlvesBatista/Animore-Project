package com.example.animoreproject.classes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.animoreproject.R;
import com.example.animoreproject.TelaMensagem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class ServiceNotificacoes extends FirebaseMessagingService {

    private static final String ID_CHANNEL_ADOCAO_SOLICITADA = "channel_adocao_solicitada";

    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    static Context ctx;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed\n" + task.getException());
                            return;
                        }

                        String token = task.getResult();
                        System.out.println("NOVO TOKEN: " + token);
                    }
                });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        System.out.println("From " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getData() != null) {
            if (remoteMessage.getMessageId().equals(ID_CHANNEL_ADOCAO_SOLICITADA)) {
                String titulo        = remoteMessage.getData().get("titulo");
                String conteudo      = remoteMessage.getData().get("conteudo");
                String nomeRemetente = remoteMessage.getData().get("remetente");
                String nomeAnimal    = remoteMessage.getData().get("animal");

                enviarNotificacaoAdocao(titulo, conteudo, nomeRemetente, nomeAnimal);
            }
        } else {
            Toast.makeText(ctx, "Falha no envio da notificação", Toast.LENGTH_SHORT).show();
        }
    }

    public static void criarCanalNotificacaoAdocao(Context context) {
        ctx = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    ID_CHANNEL_ADOCAO_SOLICITADA,
                    "Solicitacao de Adocao",
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void enviarNotificacaoAdocao(String titulo, String conteudo, String nomeRemetente, String nomeAnimal) {
        Intent intent = new Intent(ctx, TelaMensagem.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String nomeRemetenteCurto;
        String nomeAnimalCurto;

        if (nomeRemetente.length() > 20) {
            nomeRemetenteCurto = nomeRemetente.substring(0, 20);
        } else {
            nomeRemetenteCurto = nomeRemetente;
        }
        if (nomeAnimal.length() > 20) {
            nomeAnimalCurto = nomeAnimal.substring(0, 20);
        } else {
            nomeAnimalCurto = nomeAnimal;
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder = new NotificationCompat.Builder(
                        ctx,
                ID_CHANNEL_ADOCAO_SOLICITADA)
                        .setSmallIcon(R.drawable.ic_menu_animal)
                        .setContentTitle(titulo)
                        .setContentText(nomeRemetenteCurto +
                                        " " +
                                        conteudo +
                                        " " +
                                        nomeAnimalCurto)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    ID_CHANNEL_ADOCAO_SOLICITADA,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(1, notificationBuilder.build());
    }

    public void construirNotificacaoAdocao(String titulo, String conteudo, String nomeRemetente, String nomeAnimal) {
        Map<String,String> data = new HashMap<>();
        data.put("titulo",    titulo);
        data.put("conteudo",  conteudo);
        data.put("remetente", nomeRemetente);
        data.put("animal",    nomeAnimal);

        RemoteMessage remoteMessage = new RemoteMessage.Builder(
                "SERVER_ID"
                + "@gcm.googleapis.com")
                .setMessageId(ID_CHANNEL_ADOCAO_SOLICITADA)
                .setData(data)
                .build();

        onMessageReceived(remoteMessage);
    }
}
